package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.security.CustomUserDetails;
import com.example.employeemanagement.service.OtpService;
import com.example.employeemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/otp")
public class AuthController {

    private final OtpService otpService;
    private final UserService userService;

    @GetMapping("/verify")
    public String showOtpPage(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findById(userDetails.getUserId());
            otpService.generateAndSendOtp(user);
            return "otp";
        }
        return "redirect:/login";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String otp, Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findById(userDetails.getUserId());

        if (otpService.validateOtp(user, otp)) {
            userService.update_is_online(user.getId(), 1);

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("");

            return switch (role) {
                case "ROLE_ADMIN" -> "redirect:/admin/users";
                case "ROLE_IT" -> "redirect:/it";
                case "ROLE_ACCOUNTANT" -> "redirect:/accountant";
                case "ROLE_MARKETING" -> "redirect:/marketing";
                default -> "redirect:/";
            };
        }

        if (user.getFailedOtpAttempts() >= 3) {
            return "redirect:/login?error=max_attempts";
        }

        model.addAttribute("error", "Invalid OTP");
        return "otp";
    }

}
