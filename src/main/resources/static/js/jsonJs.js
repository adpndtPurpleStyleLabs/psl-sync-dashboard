function fetchJson() {
    var pathSegments = window.location.pathname.split("/");
    var eventKey = pathSegments[pathSegments.length - 1];
    let searchQuery = document.getElementById("searchInput").value;
    axios.get('/trigger/json-data/'+eventKey+"?productId="+searchQuery)
        .then(response => {
            // Convert JSON to beautified format
            let beautifiedJson = JSON.stringify(response.data, null, 4);
            document.getElementById("jsonOutput").innerHTML = "<code>" + syntaxHighlight(beautifiedJson) + "</code>";

            // Show modal
            let jsonModal = new bootstrap.Modal(document.getElementById('jsonModal'));
            jsonModal.show();
        })
        .catch(error => console.error("Error fetching JSON:", error));
}

function syntaxHighlight(json) {
    json = json.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:\s*)?|\b(true|false|null)\b|\b(-?\d+(\.\d*)?([eE][+-]?\d+)?)\b)/g,
        function (match) {
            let cls = "text-primary"; // Default color
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = "text-danger"; // Key color
                } else {
                    cls = "text-success"; // String color
                }
            } else if (/true|false/.test(match)) {
                cls = "text-warning"; // Boolean color
            } else if (/null/.test(match)) {
                cls = "text-muted"; // Null color
            } else {
                cls = "text-info"; // Number color
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
}