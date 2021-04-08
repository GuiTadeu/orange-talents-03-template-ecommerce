package com.orange.ecommerce.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private Long subCategoryId;

    public Category() {
    }

    public Category(@NotBlank String name, Optional<Long> subCategoryId) {
        this.name = name;
        this.subCategoryId = subCategoryId.orElse(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }
}
