package com.orange.ecommerce.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void create__should_not_save_new_category_with_empty_name_and_return_status_400_badRequest() throws Exception {
        URI uri = new URI("/categories");
        String json = "{\"name\": \"\"}";

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
    public void create__should_save_new_category_with_name_and_return_status_201_created() throws Exception {
        URI uri = new URI("/categories");
        String json = "{\"name\": \"Tecnologia\"}";

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
    public void create__should_not_save_new_category_with_not_registered_subCategory_and_return_status_400_badRequest() throws Exception {
        URI uri = new URI("/categories");
        String json = "{\"name\": \"Instrumentos\", \"subCategoryId\":\"2021\"}";

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
    public void create__should_save_new_category_with_registered_subCategory_and_return_status_201_created() throws Exception {
        URI uri = new URI("/categories");

        Category category = new Category("Videogames", Optional.empty());
        Category savedCategory = categoryRepository.save(category);

        String json = "{\"name\": \"Sony\","
                    + "\"subCategoryId\":\"" + savedCategory.getId() + "\"}";

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
    public void create__should_not_save_new_category_if_name_already_exists_and_return_status_400_badRequest() throws Exception {
        URI uri = new URI("/categories");

        Category category = new Category("Cafeteiras", Optional.empty());
        Category savedCategory = categoryRepository.save(category);

        String json = "{\"name\": \"Cafeteiras\"}";

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers
                .status()
                .is(400));
    }


}
