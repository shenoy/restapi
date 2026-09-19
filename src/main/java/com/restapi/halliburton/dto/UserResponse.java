package com.restapi.halliburton.dto;

import com.restapi.halliburton.entity.UserEntity; // adjust to wherever UserEntity lives

public record UserResponse(Long id, String name, String email) {
    public static UserResponse from(UserEntity u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
