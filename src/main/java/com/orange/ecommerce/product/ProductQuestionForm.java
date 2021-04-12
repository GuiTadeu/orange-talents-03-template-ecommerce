package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductQuestionForm {

    @NotBlank
    private String title;

    public ProductQuestion toModel(@NotNull User user, @NotNull Product product) {
        return new ProductQuestion(title, user, product);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
