package com.orange.ecommerce.customer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String encryptPassword;

    @NotNull
    @PastOrPresent
    private LocalDate createdAt = LocalDate.now();

    public Customer(@Email @NotBlank String email, @NotBlank @Size(min = 6) String plainPassword) {
        this.email = email.toLowerCase();
        this.encryptPassword = new BCryptPasswordEncoder().encode(plainPassword);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
