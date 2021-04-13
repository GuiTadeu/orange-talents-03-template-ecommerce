package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.category.CategoryRepository;
import com.orange.ecommerce.share.MailSender;
import com.orange.ecommerce.share.ValidationErrorsDTO;
import com.orange.ecommerce.user.User;
import com.orange.ecommerce.user.UserRepository;
import io.swagger.annotations.Api;
import javassist.tools.web.BadHttpRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
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
    private final ProductOpinionRepository productOpinionRepository;
    private final ProductQuestionRepository productQuestionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProductController(ProductRepository productRepository, ProductOpinionRepository productOpinionRepository,
                             ProductQuestionRepository productQuestionRepository, CategoryRepository categoryRepository,
                             UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productOpinionRepository = productOpinionRepository;
        this.productQuestionRepository = productQuestionRepository;
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

        User loggedUser = userRepository.findByEmail(principal.getName()).orElseThrow(BindException::new);
        Product product = productRepository.findById(productId).orElseThrow(BadHttpRequest::new);

        String imageUrl = ImageUploader.upload(image);

        if(product.isOwner(loggedUser)) {
            product.addImage(imageUrl);
            var imagesDTO = new ProductImagesDTO(product);
            return ResponseEntity.ok(imagesDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @PostMapping("/{productId}/opinion")
    public ResponseEntity<?> productOpinion(@RequestBody @Valid ProductOpinionForm form, @PathVariable Long productId, Principal principal) throws Exception {

        User loggedUser = userRepository.findByEmail(principal.getName()).orElseThrow(BadHttpRequest::new);
        Product product = productRepository.findById(productId).orElseThrow(BindException::new);

        Optional<ProductOpinion> possibleOpinionByUser = productOpinionRepository.findByUserAndProduct(loggedUser, product);
        if(possibleOpinionByUser.isPresent()) {
            var validationDTO = new ValidationErrorsDTO();
            validationDTO.addError("The user already has an opinion about this product");
            return ResponseEntity.badRequest().body(validationDTO);
        }

        ProductOpinion opinion = form.toModel(loggedUser, product);
        ProductOpinion savedProductOpinion = productOpinionRepository.save(opinion);

        var productOpinionDTO = new ProductOpinionDTO(savedProductOpinion);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/opinion/{id}")
                .buildAndExpand(productOpinionDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(productOpinionDTO);
    }

    @PostMapping("/{productId}/question")
    public ResponseEntity<?> productQuestion(@RequestBody @Valid ProductQuestionForm form, @PathVariable Long productId, Principal principal) throws Exception {

        User loggedUser = userRepository.findByEmail(principal.getName()).orElseThrow(BadHttpRequest::new);
        Product product = productRepository.findById(productId).orElseThrow(BindException::new);

        ProductQuestion question = form.toModel(loggedUser, product);
        ProductQuestion savedProductQuestion = productQuestionRepository.save(question);

        MailSender sender = new MailSender()
            .setFrom(loggedUser.getEmail())
            .setTo(product.getOwnerEmail())
            .setSubject("Nova pergunta - " + product.getName())
            .setText(form.getTitle())
            .send();

        var productQuestionDTO = new ProductQuestionDTO(savedProductQuestion);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/question/{id}")
                .buildAndExpand(productQuestionDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(productQuestionDTO);
    }

    @GetMapping("/{productId}/info")
    public ResponseEntity<?> info(@PathVariable Long productId) {

        List<LastQuestionsProjection> lastQuestions = productQuestionRepository.findByProduct_IdOrderByCreatedAtDesc(productId, PageRequest.of(0, 10));
        List<LastOpinionsProjection> lastOpinions = productOpinionRepository.findByProduct_IdOrderByCreatedAtDesc(productId, PageRequest.of(0, 10));

        ProductInfoDTO info = productRepository.getProductInfo(productId, lastQuestions, lastOpinions);
        return ResponseEntity.ok().body(info);
    }
}
