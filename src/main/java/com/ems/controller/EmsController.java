package com.ems.controller;

import com.ems.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/") // Base URL for EMS module
public class EmsController {

    private final ManagerService managerService;

    public EmsController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("")
    public String showHome() {
        return "index";
    }

    @GetMapping("loginManager")
    public String showLoginManager() {
        return "login-manager";
    }

    @PostMapping("login-manager")
    public String loginManager(@RequestParam String email, 
                               @RequestParam String password) {
        if (managerService.authenticateManager(email, password)) {
            return "redirect:/manager/dashboard"; // ✅ Redirects to Dashboard without session
        } else {
            return "redirect:loginManager?error=true";
        }
    }

    // ✅ Ensure Leave Management is mapped correctly
    @GetMapping("/manager/leaveManage")
    public String showLeaveManage() {
        return "manager/leaveManagment";
    }
}
