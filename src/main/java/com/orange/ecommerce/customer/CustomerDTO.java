package com.orange.ecommerce.customer;

import java.time.LocalDate;

public class CustomerDTO {

    private Long id;
    private String email;
    private LocalDate createdAt;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.createdAt = customer.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
