package com.example.Login.controller;

import com.example.Login.model.Dashboard;
import com.example.Login.repo.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardRepository dashboardRepository;

    @PostMapping("/new")
    public ResponseEntity createNewDashBoard(@RequestParam String dashboardName, @RequestParam String webhookKey){
        try {
            dashboardRepository.save(new Dashboard(dashboardName, webhookKey));
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dashboards")
    public ResponseEntity getAllDashBoards(){
        try{
            Optional<List<Dashboard>> allDashboards = Optional.of(dashboardRepository.findAll());
            return new ResponseEntity<>(allDashboards.get(), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
