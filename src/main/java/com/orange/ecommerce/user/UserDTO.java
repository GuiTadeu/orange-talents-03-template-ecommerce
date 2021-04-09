package com.orange.ecommerce.user;

import java.time.LocalDate;

public class UserDTO {

    private Long id;
    private String email;
    private LocalDate createdAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
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
