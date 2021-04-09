package com.orange.ecommerce.user;

import com.orange.ecommerce.share.UniqueValue;

import javax.validation.constraints.*;

public class UserForm {

    @Email
    @NotBlank
    @UniqueValue(fieldName = "email", domainClass = User.class)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String plainPassword;

    public UserForm(@Email @NotBlank String email, @NotBlank @Size(min = 6) String plainPassword) {
        this.email = email;
        this.plainPassword = plainPassword;
    }

    public User toModel() {
        return new User(email, plainPassword);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
}
