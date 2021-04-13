package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ProductQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Product product;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductQuestion() {

    }

    public ProductQuestion(@NotBlank String title, @NotNull User user, @NotNull Product product) {
        this.title = title;
        this.user = user;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
