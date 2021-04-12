package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.share.UniqueValue;
import com.orange.ecommerce.user.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @NotNull
    @ManyToOne
    private User owner;

    @ElementCollection
    @CollectionTable(name="product_images", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image_url")
    private List<String> imagesUrl = new ArrayList<>();

    public Product() {

    }

    public Product(@NotBlank String name, @Min(1) @NotNull BigDecimal price, @Min(0) @NotNull Integer quantity,
                   @NotNull @Size(min = 3) Map<String, String> specifications,
                   @NotBlank @Size(max = 100) String description, @NotNull Category category, @NotNull User owner) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.specifications = specifications;
        this.description = description;
        this.category = category;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void addImage(String image) {
        imagesUrl.add(image);
    }

    public boolean isOwner(User user) {
        return user.equals(this.owner);
    }

    public String getOwnerEmail() {
        return owner.getEmail();
    }
}
