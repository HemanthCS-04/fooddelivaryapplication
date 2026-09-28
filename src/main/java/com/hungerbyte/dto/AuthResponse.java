package com.hungerbyte.dto;

import com.hungerbyte.entity.Role;

public class AuthResponse {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private Role role;
    private String token; // Simple bearer or mock session token

    public AuthResponse() {}

    public AuthResponse(Long id, String name, String email, String mobile, Role role, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
