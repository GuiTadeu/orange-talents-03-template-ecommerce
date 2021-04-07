package com.orange.ecommerce.customer;

import com.orange.ecommerce.share.UniqueValue;

import javax.validation.constraints.*;

public class CustomerForm {

    @Email
    @NotBlank
    @UniqueValue(fieldName = "email", domainClass = Customer.class)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String plainPassword;

    public CustomerForm(@Email @NotBlank String email, @NotBlank @Size(min = 6) String plainPassword) {
        this.email = email;
        this.plainPassword = plainPassword;
    }

    public Customer toModel() {
        return new Customer(email, plainPassword);
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
