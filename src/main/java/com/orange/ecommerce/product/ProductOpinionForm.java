package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductOpinionForm {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;

    public ProductOpinion toModel(User user, Product product) {
        return new ProductOpinion(title, description, rating, user, product);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
