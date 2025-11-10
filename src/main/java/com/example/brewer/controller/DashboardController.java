// Em: src/main/java/com/example/brewer/controller/DashboardController.java
package com.example.brewer.controller;

import com.example.brewer.model.DashboardDTO;
import com.example.brewer.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String exibirDashboard(Model model) {
        DashboardDTO dados = dashboardService.getDadosDashboard();
        model.addAttribute("dados", dados);
        return "dashboard"; // Nome do arquivo HTML
    }
}