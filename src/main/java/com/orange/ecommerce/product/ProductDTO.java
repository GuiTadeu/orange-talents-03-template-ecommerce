package com.orange.ecommerce.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ProductDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Map<String, String> specifications;
    private String description;
    private String categoryName;
    private LocalDate createdAt;
    private String ownerEmail;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.specifications = product.getSpecifications();
        this.description = product.getDescription();
        this.categoryName = product.getCategoryName();
        this.createdAt = product.getCreatedAt();
        this.ownerEmail = product.getOwnerEmail();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
}
