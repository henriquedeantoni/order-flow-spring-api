package com.orderflow.orderflow_api.secutiry.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 4, max = 25)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 25)
    private String password;

    @NotBlank
    @Size(max=25)
    private String firstName;

    @NotBlank
    @Size(max=25)
    private String lastName;

    private Set<String> roles;
}
