package com.orange.ecommerce.category;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class CategoryDTO {

    private Long id;
    private String name;
    private String subCategory;

    public CategoryDTO(@NotNull Category category, Optional<Category> subCategory) {
        this.id = category.getId();
        this.name = category.getName();
        subCategory.ifPresent(subCat -> this.subCategory = subCat.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubCategory() {
        return subCategory;
    }
}
