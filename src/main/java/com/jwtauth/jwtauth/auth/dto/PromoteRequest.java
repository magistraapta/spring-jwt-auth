package com.jwtauth.jwtauth.auth.dto;

import com.jwtauth.jwtauth.auth.entity.Role;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteRequest {
    private Role role;
    private Long id;
}
