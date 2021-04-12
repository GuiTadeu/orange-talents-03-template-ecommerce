package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class ProductOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Product product;

    public ProductOpinion() {

    }

    public ProductOpinion(@NotBlank String title, @NotBlank String description,
                          @NotNull @Range(min = 1, max = 5) Integer rating,
                          @NotNull User user, @NotNull Product product) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.user = user;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRating() {
        return rating;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }
}
