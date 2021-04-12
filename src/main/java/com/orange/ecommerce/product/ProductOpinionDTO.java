package com.orange.ecommerce.product;

public class ProductOpinionDTO {

    private Long id;
    private String title;
    private String description;
    private Integer rating;
    private Long userId;
    private Long productId;

    public ProductOpinionDTO(ProductOpinion opinion) {
        this.id = opinion.getId();
        this.title = opinion.getTitle();
        this.description = opinion.getDescription();
        this.rating = opinion.getRating();
        this.userId = opinion.getUser().getId();
        this.productId = opinion.getProduct().getId();
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

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }
}
