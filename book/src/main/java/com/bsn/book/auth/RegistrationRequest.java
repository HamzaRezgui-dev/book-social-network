package com.bsn.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be empty")
    private String firstname;
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be empty")
    private String lastname;
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;
}
