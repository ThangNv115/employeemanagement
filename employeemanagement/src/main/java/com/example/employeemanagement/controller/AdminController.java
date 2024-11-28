package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        if (authentication != null) {
            System.out.println("User: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
        }
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute User user, @RequestParam String role,
            RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user, role);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/create";
        }
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("user", userService.findById(id));
            return "edit-user";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id,
            @ModelAttribute User user,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user, role);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:users";
    }
}