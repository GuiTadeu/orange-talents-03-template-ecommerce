package com.orange.ecommerce.config.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserLoginForm {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String plainPassword;

    public UsernamePasswordAuthenticationToken build() {
        return new UsernamePasswordAuthenticationToken(this.email, this.plainPassword);
    }

    public String getEmail() {
        return email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }
}