package com.example.employeemanagement.security;

import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.service.WebSocketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import jakarta.servlet.ServletException;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketService webSocketService;

    public CustomLogoutSuccessHandler() {
        setDefaultTargetUrl("/login");
    }

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            String username = authentication.getName();
            userRepository.findByUsername(username)
                    .ifPresent(user -> {
                        userRepository.updateUserOnlineStatus(user.getId(), 0);
                        webSocketService.notifyStatusChange(user.getId(), 0);
                    });
        }
        super.onLogoutSuccess(request, response, authentication);
    }
}