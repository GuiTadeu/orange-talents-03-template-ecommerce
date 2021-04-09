package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.share.MustExistsOnDatabase;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashMap;

public class ProductForm {

    @NotBlank
    private String name;

    @Min(1)
    @NotNull
    private BigDecimal price;

    @Min(0)
    @NotNull
    private Integer quantity;

    @NotNull
    @Size(min = 3)
    private HashMap<String, String> specifications;

    @NotBlank
    @Size(max = 100)
    private String description;

    @NotNull
    @MustExistsOnDatabase(fieldName = "id", isOptionalAttribute = false, domainClass = Category.class)
    private Long categoryId;

    public ProductForm(@NotBlank String name, @Min(1) @NotNull BigDecimal price, @Min(0) @NotNull Integer quantity,
                       @NotNull @Size(min = 3) HashMap<String, String> specifications,
                       @NotBlank @Size(max = 100) String description, @NotNull Long categoryId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.specifications = specifications;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Product toModel(Category category) {
        return new Product(name, price, quantity, specifications, description, category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public HashMap<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(HashMap<String, String> specifications) {
        this.specifications = specifications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
