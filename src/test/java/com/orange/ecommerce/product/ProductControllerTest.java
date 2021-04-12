package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;

    private User kratos;
    private User atreus;

    @BeforeEach
    public void setup() {
        kratos = new User("kratos@gmail.com", "kratos123");
        atreus = new User("atreus@gmail.com", "atreus123");

        manager.persist(kratos);
        manager.persist(atreus);
    }

    @Test
    @Transactional
    public void create__should_not_save_new_product_if_json_is_empty_and_return_status_400_badRequest() throws Exception {
        login("kratos@gmail.com", "kratos123", "ROLE_CUSTOMER");
        postProductWithExpectedStatus("kratos@gmail.com", new URI("/products"), "{}", 400);
    }

    @Test
    @Transactional
    public void uploadImage__should_success_if_user_is_owner_of_product_and_return_status_200_ok() throws Exception {
        var product = saveProductWithOwnerLogin("God of War", "kratos@gmail.com", "kratos123");
        uploadImageWithExpectedStatus(product, 200);
    }

    @Test
    @Transactional
    public void uploadImage__should_not_upload_if_user_not_is_owner_of_product() throws Exception {
        var product = saveProductWithOwnerLogin("God of War","kratos@gmail.com", "kratos123");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");
        uploadImageWithExpectedStatus(product, 403);
    }

    private void uploadImageWithExpectedStatus(Product product, Integer expectedStatus) throws Exception {
        var uploadImageProductUri = new URI(String.format("/products/%s/upload", product.getId()));

        var file = new MockMultipartFile(
                "image",
                "image01.png",
                MediaType.IMAGE_PNG_VALUE,
                "image".getBytes());

        mockMvc
            .perform(MockMvcRequestBuilders
                .multipart(uploadImageProductUri)
                .file(file))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(expectedStatus));
    }

    @Transactional
    private Product saveProductWithOwnerLogin(String productName, String ownerEmail, String ownerPassword) throws Exception {
        login(ownerEmail, ownerPassword, "ROLE_CUSTOMER");

        var category = new Category("Jogos", Optional.empty());
        manager.persist(category);

        String json = "{ \"categoryId\": " + category.getId() + ","
                    + "\"description\": \"TXT\","
                    + "\"name\": \"" + productName + "\","
                    + "\"price\": 350,"
                    + "\"quantity\": 20,"
                    + "\"specifications\": { "
                        + "\"X\": \"A\","
                        + "\"Y\": \"B\","
                        + "\"Z\": \"C\""
                        + "}"
                    + "}";

        var uri = new URI("/products");
        postProductWithExpectedStatus(ownerEmail, uri, json, 201);

        return productRepository.getByName(productName);
    }

    private void postProductWithExpectedStatus(String ownerEmail, URI uri, String json, Integer expectedStatus) throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(expectedStatus))
            .andExpect(expectedStatus < 300
                    ? jsonPath("$.ownerEmail").value(ownerEmail)
                    : matchAll());
    }

    private void login(String username, String password, String role) {
        logout();
        SecurityContextHolder.getContext()
                .setAuthentication(new PreAuthenticatedAuthenticationToken(username, password,
                        Arrays.asList(new SimpleGrantedAuthority(role))));
    }

    private void logout() {
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    }
}
