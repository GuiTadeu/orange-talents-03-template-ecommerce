package com.orange.ecommerce.product;

public class ProductQuestionDTO {

    private Long id;
    private String title;
    private Long userId;
    private Long productId;

    public ProductQuestionDTO(ProductQuestion productQuestion) {
        this.id = productQuestion.getId();
        this.title = productQuestion.getTitle();
        this.userId = productQuestion.getUserId();
        this.productId = productQuestion.getProductId();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }
}
