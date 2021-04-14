package com.orange.ecommerce.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductInfoDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;
    private Map<String, String> specifications;
    private List<String> images;
    private Float averageRatings;
    private Integer totalRatings;
    private List<LastQuestionsProjection> lastQuestions;
    private List<LastOpinionsProjection> lastOpinions;

    public ProductInfoDTO(Product product, ProductRatingsProjection ratings,
                          List<LastQuestionsProjection> lastQuestions, List<LastOpinionsProjection> lastOpinions) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity = product.getStock();
        this.categoryName = product.getCategoryName();
        this.specifications = product.getSpecifications();
        this.images = product.getImagesUrl();
        this.averageRatings = ratings.getAverageRatings();
        this.totalRatings = ratings.getTotalRatings();
        this.lastQuestions = lastQuestions;
        this.lastOpinions = lastOpinions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public List<String> getImages() {
        return images;
    }

    public Float getAverageRatings() {
        return averageRatings;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }

    public List<LastQuestionsProjection> getLastQuestions() {
        return lastQuestions;
    }

    public List<LastOpinionsProjection> getLastOpinions() {
        return lastOpinions;
    }
}
