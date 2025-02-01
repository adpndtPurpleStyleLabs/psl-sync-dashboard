package com.example.Login.Dao;

import com.example.Login.dto.DayWiseCountDto;
import com.example.Login.dto.SearchDto;
import com.example.Login.dto.WebhookApiPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createATable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    event_status VARCHAR(10) NOT NULL,\n" +
                "    payload TEXT NOT NULL,\n" +
                "    count INTEGER NOT NULL,\n" +
                "    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");";
        jdbcTemplate.execute(sql);
    }

    public Integer getLiveSyncProductCount(String tableName, String rate, String rateTime) {
        String sql = """
                SELECT COALESCE(SUM(count), 0) AS total_sum
                    FROM @tableName
                WHERE 
                    datetime(received_at, 'localtime') >= datetime(CURRENT_TIMESTAMP, '-@rate @time', 'localtime'); 
                """;
        sql = sql.replaceAll("@tableName", tableName);
        sql = sql.replaceAll("@rate", rate);
        sql = sql.replaceAll("@time", rateTime);
        return jdbcTemplate.queryForObject(sql, Integer.class);

    }

    public Integer getDayProductCountWithStatus(String tableName, String status) {
        String sql = """
                SELECT
                    COALESCE(SUM(count), 0) AS total_sum
                FROM @tableName
                    WHERE
                DATE(received_at, 'localtime') = DATE('now', 'localtime')
                AND event_status =  '@status'
                """;
        sql = sql.replaceAll("@tableName", tableName);
        sql = sql.replaceAll("@status", status);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<DayWiseCountDto> getWeekData(String tableName) {
        String sql = """
                    WITH RECURSIVE date_series AS (
                        SELECT DATE('now', '-6 days') AS day
                        UNION ALL
                        SELECT DATE(day, '+1 day') FROM date_series WHERE day < DATE('now')
                    )
                    SELECT 
                        strftime('%m/%d', ds.day) AS day,
                        COALESCE(SUM(ped.count), 0) AS total_count,  
                        COALESCE(SUM(SUM(CASE WHEN ped.event_status = 'PASSED' THEN ped.count ELSE 0 END)) 
                                 OVER (ORDER BY ds.day), 0) AS cumulative_passed,
                        COALESCE(SUM(SUM(CASE WHEN ped.event_status = 'FAILED' THEN ped.count ELSE 0 END)) 
                                 OVER (ORDER BY ds.day), 0) AS cumulative_failed
                    FROM 
                        date_series ds
                    LEFT JOIN 
                       @tableName ped
                    ON 
                        DATE(ped.received_at) = ds.day
                    WHERE 
                        ds.day >= DATE('now', '-6 days')
                    GROUP BY 
                        ds.day
                    ORDER BY 
                        ds.day ASC;
                """;
        sql = sql.replaceAll("@tableName", tableName);
        return jdbcTemplate.query(sql, mapDashboardStatsRow());

    }

    private RowMapper<DayWiseCountDto> mapDashboardStatsRow() {
        return (rs, rowNum) -> new DayWiseCountDto(
                rs.getString("day"),
                rs.getInt("cumulative_passed"),
                rs.getInt("cumulative_failed")
        );
    }

    public List<SearchDto> getSyncedProductIds(String tableName, int page, int size) {
        String sql = """
                          
                    SELECT
                    received_at AS receivedAt,
                    payload AS productIds,
                    event_status As status
                FROM
                    @tableName
                ORDER BY
                    received_at DESC
                LIMIT ? OFFSET (? - 1) * ?;
                """;
        sql = sql.replaceAll("@tableName", tableName);
        return jdbcTemplate.query(sql, new Object[]{size, page, size}, mapSearchDto());
    }

    public List<SearchDto> getSyncedProductIdsWithSearch(String tableName, int page, int size, String productId) {
        String sql = """
                SELECT 
                      received_at AS receivedAt, 
                      payload AS productIds,
                      event_status As status
                  FROM 
                      @tableName
                  WHERE 
                      payload LIKE ?
                  ORDER BY
                      received_at DESC
                  LIMIT ? OFFSET (? - 1) * ?;
                  """;
        sql = sql.replaceAll("@tableName", tableName);
        return jdbcTemplate.query(sql, new Object[]{"%" + productId + "%", size, page, size}, mapSearchDto());
    }

    private RowMapper<SearchDto> mapSearchDto() {
        return (rs, rowNum) -> new SearchDto(
                rs.getString("receivedAt"),
                rs.getString("productIds"),
                rs.getString("status")
        );
    }

    private String sanitizeTableName(String eventKey) {
        return eventKey.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();
    }

    public void triggerWebhook(WebhookApiPayload payload, String productIdsString, long lengthOfProductIds) {
        String insertQuery = "INSERT INTO " + sanitizeTableName(payload.getEventKey()) + " (event_status, payload, count) VALUES ('" + payload.getEventStatus() + "', '" + productIdsString + "', " + lengthOfProductIds + ");";
        jdbcTemplate.update(insertQuery);
    }

    public void deleteWebhook(String eventKey) {
        jdbcTemplate.execute(" DELETE FROM webhooks WHERE event_key = '" + eventKey.trim() + "'");
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + eventKey.trim());
    }
}