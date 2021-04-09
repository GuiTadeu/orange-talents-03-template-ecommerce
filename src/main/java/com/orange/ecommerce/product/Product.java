package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @ElementCollection
    @MapKeyColumn(name="specification_name")
    @Column(name="specification_value")
    private Map<String, String> specifications;

    @NotBlank
    @Size(max = 100)
    private String description;

    @NotNull
    @ManyToOne
    private Category category;

    @PastOrPresent
    private LocalDate createdAt = LocalDate.now();

    public Product(@NotBlank String name, @Min(1) @NotNull BigDecimal price, @Min(0) @NotNull Integer quantity,
                   @NotNull @Size(min = 3) HashMap<String, String> specifications,
                   @NotBlank @Size(max = 100) String description, @NotNull Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.specifications = specifications;
        this.description = description;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category.getName();
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
