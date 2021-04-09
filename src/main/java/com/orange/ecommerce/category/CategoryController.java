package com.orange.ecommerce.category;

import com.orange.ecommerce.config.security.role.AllowAdmin;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @AllowAdmin
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CategoryForm form) {
        Category category = categoryRepository.save(form.toModel());

        Optional<Category> subCategory = Optional.empty();
        if (form.hasSubCategory()) {
            subCategory = categoryRepository.findById(category.getSubCategoryId());
        }

        CategoryDTO categoryDTO = new CategoryDTO(category, subCategory);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(categoryDTO);
    }
}
