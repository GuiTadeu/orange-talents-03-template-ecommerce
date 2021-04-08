package com.orange.ecommerce.category;

import com.orange.ecommerce.share.MustExistsOnDatabase;
import com.orange.ecommerce.share.UniqueValue;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class CategoryForm {

    @NotBlank
    @UniqueValue(fieldName = "name", domainClass = Category.class)
    private String name;

    @MustExistsOnDatabase(fieldName = "id", isOptionalAttribute = true, domainClass = Category.class)
    private Long subCategoryId;

    public CategoryForm(@NotBlank String name, Long subCategoryId) {
        this.name = name;
        this.subCategoryId = subCategoryId;
    }

    public Category toModel() {
        return new Category(name, Optional.ofNullable(subCategoryId));
    }

    public boolean hasSubCategory() {
        return nonNull(subCategoryId) && subCategoryId != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
