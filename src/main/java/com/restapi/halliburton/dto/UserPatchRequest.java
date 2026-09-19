package com.restapi.halliburton.dto;

import jakarta.validation.constraints.Email;


public record UserPatchRequest(String name, @Email String email) {}
