package com.rajat.quickpick.controller;

import com.rajat.quickpick.dto.auth.AuthResponseDto;
import com.rajat.quickpick.dto.user.LoginUserDto;
import com.rajat.quickpick.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> adminLogin(@Valid @RequestBody LoginUserDto loginDto) {
        AuthResponseDto response = adminService.adminLogin(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthResponseDto> createAdmin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String fullName = request.get("fullName");

        AuthResponseDto response = adminService.createAdmin(email, password, fullName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, String>> deleteAdmin(@PathVariable String email) {
        adminService.deleteAdmin(email);
        return ResponseEntity.ok(Map.of("message", "Admin deleted successfully"));
    }
}