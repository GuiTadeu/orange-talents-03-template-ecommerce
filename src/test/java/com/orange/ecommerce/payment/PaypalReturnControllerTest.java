package com.orange.ecommerce.payment;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.product.*;
import com.orange.ecommerce.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.orange.ecommerce.payment.PaymentMethod.PAYPAL;
import static com.orange.ecommerce.payment.PaymentStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class PaypalReturnControllerTest {

    private final String SUCCESS = "1";
    private final String ERROR = "0";

    @PersistenceContext
    private EntityManager manager;

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductBuyRepository productBuyRepository;
    @Autowired private GatewayTransactionRepository gatewayTransactionRepository;

    private User kratos;
    private User atreus;

    private ProductBuy paypalProductBuy;

    @BeforeEach
    void setup() throws Exception {
        kratos = new User("kratos@gmail.com", "kratos123");
        atreus = new User("atreus@gmail.com", "atreus123");

        manager.persist(kratos);
        manager.persist(atreus);

        paypalProductBuy = saveProductBuy("Nespresso",
                "kratos@gmail.com", "kratos123", PAYPAL);
    }

    @Test
    @Transactional
    public void productPurchasedReturn__create_new_transaction_should_return_gatewayStatus_success_and_altered_payment_status_to_paid() throws Exception {
        assertEquals(paypalProductBuy.getStatus(), STARTED);
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        URI paymentReturnUrl = new URI("/gateway/paypal/productBuy/" + paypalProductBuy.getId() + "/return");
        GatewayReturn transactionResponse = new GatewayReturn(12039851L, SUCCESS);

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders
                .post(paymentReturnUrl)
                .content(new Gson().toJson(transactionResponse))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(200))
            .andReturn();

        Integer paymentId = JsonPath.read(result.getResponse().getContentAsString(), "$.paymentId");
        GatewayTransaction lastPaymentTransaction = gatewayTransactionRepository
                .findTopByPaymentIdOrderByCreatedAtDesc(paymentId.longValue()).orElseThrow();

        assertEquals(PAID, lastPaymentTransaction.getBuyStatus());
        assertEquals(SUCCESS, lastPaymentTransaction.getStatus());
        assertEquals(12039851L, lastPaymentTransaction.getPaymentId());
        assertEquals(PAYPAL, lastPaymentTransaction.getPaymentMethod());
        assertEquals("Nespresso", lastPaymentTransaction.getProductName());
    }

    @Test
    @Transactional
    public void productPurchasedReturn__create_new_transaction_should_return_gatewayStatus_error_and_alter_payment_status_to_pending() throws Exception {
        assertEquals(paypalProductBuy.getStatus(), STARTED);
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        URI paymentReturnUrl = new URI("/gateway/paypal/productBuy/" + paypalProductBuy.getId() + "/return");

        String json = new Gson().toJson(new GatewayReturn(12039851L, ERROR));
        MvcResult result = post(paymentReturnUrl, json, 200);

        Integer paymentId = JsonPath.read(result.getResponse().getContentAsString(), "$.paymentId");
        GatewayTransaction lastPaymentTransaction = gatewayTransactionRepository
                .findTopByPaymentIdOrderByCreatedAtDesc(paymentId.longValue()).orElseThrow();

        assertEquals(PENDING, lastPaymentTransaction.getBuyStatus());
        assertEquals(ERROR, lastPaymentTransaction.getStatus());
        assertEquals(12039851L, lastPaymentTransaction.getPaymentId());
        assertEquals(PAYPAL, lastPaymentTransaction.getPaymentMethod());
        assertEquals("Nespresso", lastPaymentTransaction.getProductName());
    }

    @Test
    @Transactional
    public void productPurchasedReturn__not_create_new_transaction_if_gateway_status_is_already_success() throws Exception {
        assertEquals(paypalProductBuy.getStatus(), STARTED);
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        URI paymentReturnUrl = new URI("/gateway/paypal/productBuy/" + paypalProductBuy.getId() + "/return");

        String successJson = new Gson().toJson(new GatewayReturn(12039851L, SUCCESS));
        post(paymentReturnUrl, successJson, 200);

        String errorJson = new Gson().toJson(new GatewayReturn(12039851L, ERROR));
        post(paymentReturnUrl, errorJson, 400);
    }

    @Test
    @Transactional
    public void productPurchasedReturn__create_new_transaction_if_payment_id_is_already_exists_and_status_not_is_success() throws Exception {
        assertEquals(paypalProductBuy.getStatus(), STARTED);
        login("atreus@gmail.com", "atreus123", "ROLE_CUSTOMER");

        URI paymentReturnUrl = new URI("/gateway/paypal/productBuy/" + paypalProductBuy.getId() + "/return");

        String errorJson = new Gson().toJson(new GatewayReturn(12039851L, ERROR));

        post(paymentReturnUrl, errorJson, 200);
        post(paymentReturnUrl, errorJson, 200);
        post(paymentReturnUrl, errorJson, 200);

        List<GatewayTransaction> allTransactionalErrors = gatewayTransactionRepository.findAllByPaymentId(12039851L);
        assertEquals(allTransactionalErrors.size(), 3);

        String successJson = new Gson().toJson(new GatewayReturn(12039851L, SUCCESS));
        post(paymentReturnUrl, successJson, 200);

        allTransactionalErrors = gatewayTransactionRepository.findAllByPaymentId(12039851L);
        assertEquals(allTransactionalErrors.size(), 4);

        post(paymentReturnUrl, errorJson, 400);

        allTransactionalErrors = gatewayTransactionRepository.findAllByPaymentId(12039851L);
        assertEquals(allTransactionalErrors.size(), 4);
    }

    @Transactional
    private ProductBuy saveProductBuy(String productName, String ownerEmail, String ownerPassword, PaymentMethod method) throws Exception {
        login(ownerEmail, ownerPassword, "ROLE_CUSTOMER");

        var category = new Category("Jogos", Optional.empty());
        manager.persist(category);

        String productJson = "{\"categoryId\": " + category.getId() + ","
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

        post(new URI("/products"), productJson, 201);

        Product product = productRepository.getByName(productName);
        String buyUrl = String.format("/products/%s/buy", product.getId());

        var productBuyForm = new ProductBuyForm(5, method);
        MvcResult result = post(new URI(buyUrl), new Gson().toJson(productBuyForm), 307);
        Integer productBuyId = JsonPath.read(result.getResponse().getContentAsString(), "$.buyId");

        return productBuyRepository.getOne(productBuyId.longValue());
    }

    private MvcResult post(URI uri, String json, Integer expectedStatus) throws Exception {
        return mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(expectedStatus))
            .andReturn();
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
