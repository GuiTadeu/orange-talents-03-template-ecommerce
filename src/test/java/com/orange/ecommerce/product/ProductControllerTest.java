package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    public void create__should_not_save_new_product_and_return_status_400_badRequest() throws Exception {
        URI uri = new URI("/products");
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
    @WithMockUser(authorities = "ROLE_CUSTOMER")
    public void create__should_save_new_product_and_return_status_201_created() throws Exception {

        Category category = new Category("Jogos", Optional.empty());
        Category savedCategory = categoryRepository.save(category);

        URI uri = new URI("/products");
        String json = "{ \"categoryId\": " + savedCategory.getId() + ","
                        + "\"description\": \"Garoto\","
                        + "\"name\": \"God of War\","
                        + "\"price\": 80,"
                        + "\"quantity\": 21,"
                        + "\"specifications\": { "
                            + "\"Lançamento\": \"2018-04-20\","
                            + "\"Plataforma\": \"PS4\","
                            + "\"Formato\": \"Físico\""
                        + "}"
                    + "}";

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(201));
    }
}
