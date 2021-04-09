package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.category.CategoryRepository;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid ProductForm form) {

        Optional<Category> category = categoryRepository.findById(form.getCategoryId());
        if(category.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Product savedProduct = productRepository.save(form.toModel(category.get()));

        ProductDTO productDTO = new ProductDTO(savedProduct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(productDTO);
    }
}
