package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Web controller for serving dashboard pages
 * Handles navigation and page rendering
 */
@Controller
public class DashboardWebController {

    /**
     * Main dashboard page
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("title", "Real-Time Data Visualization Dashboard");
        return "dashboard";
    }

    /**
     * Dashboard home page (alternative route)
     */
    @GetMapping("/dashboard")
    public String dashboardHome(Model model) {
        return dashboard(model);
    }

    /**
     * Charts page
     */
    @GetMapping("/charts")
    public String charts(Model model) {
        model.addAttribute("title", "Charts - Dashboard");
        return "charts";
    }

    /**
     * Data management page
     */
    @GetMapping("/data")
    public String data(Model model) {
        model.addAttribute("title", "Data Management - Dashboard");
        return "data";
    }

    /**
     * Settings page
     */
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Settings - Dashboard");
        return "settings";
    }

    /**
     * API documentation page
     */
    @GetMapping("/api/docs")
    public String apiDocs(Model model) {
        model.addAttribute("title", "API Documentation - Dashboard");
        return "api-docs";
    }

    /**
     * About page
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Real-Time Dashboard");
        model.addAttribute("version", "1.0");
        model.addAttribute("author", "Aniket Mane");
        return "about";
    }

    /**
     * Chart detail page
     */
    @GetMapping("/chart/{id}")
    public String chartDetail(@PathVariable Long id, Model model) {
        model.addAttribute("title", "Chart Details - Dashboard");
        model.addAttribute("chartId", id);
        return "chart-detail";
    }
}