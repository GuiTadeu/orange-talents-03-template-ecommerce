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
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @Transactional
    public void productOpinion__should_create_an_opinion_to_product_with_user_and_return_status_201_created() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");
        String json = "{\"title\":\"Muito bom!\","
                      + "\"description\":\"Chegou antes do prazo\","
                      + "\"rating\":5,"
                      + "\"productId\":" + product.getId() + "}";

        URI uri = new URI("/products/" + product.getId() + "/opinion");
        login("kratos@gmail.com", "kratos123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(201));
    }

    @Test
    @Transactional
    public void productOpinion__should_not_create_an_opinion_and_return_status_400_badRequest() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        URI uri = new URI("/products/" + product.getId() + "/opinion");
        String json = "{}";

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400));
    }

    @Test
    @Transactional
    public void productOpinion__should_not_create_an_opinion_if_user_already_has_commented_and_return_status_400_badRequest() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");
        String firstOpinion = "{\"title\":\"Muito bom!\","
                + "\"description\":\"Chegou antes do prazo\","
                + "\"rating\":5,"
                + "\"productId\":" + product.getId() + "}";

        URI uri = new URI("/products/" + product.getId() + "/opinion");
        login("kratos@gmail.com", "kratos123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(firstOpinion)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(201));

        String secondOpinion = "{\"title\":\"Odiei!\","
                + "\"description\":\"Só não dou 0 pq não posso\","
                + "\"rating\":1,"
                + "\"productId\":" + product.getId() + "}";

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(firstOpinion)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400));
    }

    @Test
    @Transactional
    public void productQuestion_should_create_user_question_and_return_status_201_created() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");
        String userQuestion = "{\"title\": \"Como baixo tinta pra minha impressora?\"}";

        URI uri = new URI("/products/" + product.getId() + "/question");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(userQuestion)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(201));
    }

    @Test
    @Transactional
    public void productQuestion_should_not_create_user_question_and_return_status_400_badRequest() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        String userQuestion = "{\"title\":\"\"}";

        URI uri = new URI("/products/" + product.getId() + "/question");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(userQuestion)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400));
    }

    @Test
    @Transactional
    public void info__should_return_productInfo_and_status_200_ok_with_body() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        product.addImage("/image-1234.png");

        var question = new ProductQuestion("Como chegar na montanha?", atreus, product);
        var opinion = new ProductOpinion("Achei bom", "Jogo da hora, jogo bem feito", 5, atreus, product);

        manager.persist(question);
        manager.persist(opinion);

        URI uri = new URI("/products/" + product.getId() + "/info");

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(uri)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(200))
            .andExpect(jsonPath("$.name").value("Hitman"))
            .andExpect(jsonPath("$.description").value("TXT"))
            .andExpect(jsonPath("$.price").value(new BigDecimal(350)))
            .andExpect(jsonPath("$.quantity").value(20))
            .andExpect(jsonPath("$.categoryName").value("Jogos"))
            .andExpect(jsonPath("$.specifications.X").value("A"))
            .andExpect(jsonPath("$.specifications.Y").value("B"))
            .andExpect(jsonPath("$.specifications.Z").value("C"))
            .andExpect(jsonPath("$.images[0]").value("/image-1234.png"))
            .andExpect(jsonPath("$.averageRatings").value(5))
            .andExpect(jsonPath("$.totalRatings").value(1))
            .andExpect(jsonPath("$.lastQuestions[0].title").value("Como chegar na montanha?"))
            .andExpect(jsonPath("$.lastOpinions[0].title").value("Achei bom"));
    }

    @Test
    @Transactional
    public void productBuy_should_not_create_buy_and_return_status_400_badRequest() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        String productBuyForm = "{}";

        URI uri = new URI("/products/" + product.getId() + "/buy");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(productBuyForm)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400));
    }

    @Test
    @Transactional
    public void productBuy_should_create_buy_and_redirect_url_with_status_307_temporary_redirect() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        String productBuyForm = "{\"quantity\": 4, \"paymentMethod\":\"PAYPAL\"}";

        URI uri = new URI("/products/" + product.getId() + "/buy");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(productBuyForm)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(307))
            .andExpect(redirectedUrlPattern("https://paypal.com?buyId={[0-9]+}&redirectUrl=/buy/{[0-9]+}/success"));
    }

    @Test
    @Transactional
    public void productBuy_should_not_create_buy_if_request_quantity_is_more_than_stock_and_return_status_400_badRequest() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        String productBuyForm = "{\"quantity\": 412395, \"paymentMethod\":\"PAYPAL\"}";

        URI uri = new URI("/products/" + product.getId() + "/buy");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(productBuyForm)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400))
            .andExpect(jsonPath("$.globalErrorMessages[0]").value("The quantity is greater than stock"));
    }

    @Test
    @Transactional
    public void productBuy_should_decrement_stock_if_success() throws Exception {
        Product product = saveProductWithOwnerLogin("Hitman", "kratos@gmail.com", "kratos123");

        assertEquals(20, product.getStock());

        String productBuyForm = "{\"quantity\": 10, \"paymentMethod\":\"PAYPAL\"}";

        URI uri = new URI("/products/" + product.getId() + "/buy");
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(productBuyForm)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(307))
            .andExpect(redirectedUrlPattern("https://paypal.com?buyId={[0-9]+}&redirectUrl=/buy/{[0-9]+}/success"));

        assertEquals(10, product.getStock());
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
