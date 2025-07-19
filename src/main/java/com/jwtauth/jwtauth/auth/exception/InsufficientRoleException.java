package com.jwtauth.jwtauth.auth.exception;

public class InsufficientRoleException extends RuntimeException {
    
    private final String requiredRole;
    private final String userRole;

    public InsufficientRoleException(String requiredRole, String userRole) {
        super(String.format("Access denied. Required role: %s, User role: %s", requiredRole, userRole));
        this.requiredRole = requiredRole;
        this.userRole = userRole;
    }

    public InsufficientRoleException(String message) {
        super(message);
        this.requiredRole = null;
        this.userRole = null;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public String getUserRole() {
        return userRole;
    }
} 