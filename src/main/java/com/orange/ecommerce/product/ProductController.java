package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.category.CategoryRepository;
import com.orange.ecommerce.user.User;
import com.orange.ecommerce.user.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.BindException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ProductForm form, Principal principal) {

        Optional<Category> category = categoryRepository.findById(form.getCategoryId());
        if(category.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User loggedUser = userRepository.getByEmail(principal.getName());
        Product savedProduct = productRepository.save(form.toModel(category.get(), loggedUser));

        var productDTO = new ProductDTO(savedProduct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(productDTO);
    }

    @Transactional
    @PostMapping("/{productId}/upload")
    public ResponseEntity<?> uploadImage(@RequestPart(name = "image") MultipartFile image, @PathVariable Long productId, Principal principal) throws Exception {

        Product product = productRepository.findById(productId).orElseThrow(BindException::new);
        User loggedUser = userRepository.findByEmail(principal.getName()).orElseThrow(BindException::new);

        String imageUrl = ImageUploader.upload(image);

        if(product.isOwner(loggedUser)) {
            product.addImage(imageUrl);
            var imagesDTO = new ProductImagesDTO(product);
            return ResponseEntity.ok(imagesDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
