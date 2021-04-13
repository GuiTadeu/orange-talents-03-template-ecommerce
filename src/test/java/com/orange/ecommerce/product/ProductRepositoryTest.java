package com.orange.ecommerce.product;

import com.orange.ecommerce.category.Category;
import com.orange.ecommerce.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductQuestionRepository productQuestionRepository;
    @Autowired private ProductOpinionRepository productOpinionRepository;

    private Product product;

    @BeforeEach
    public void setup() {

        var ownerUser = new User("kratos@gmail.com", "kratos123");
        var firstCustomer = new User("atreus@gmail.com", "atreus123");
        var secondCustomer = new User("mimir@gmail.com", "mimir123");

        manager.persist(ownerUser);
        manager.persist(firstCustomer);
        manager.persist(secondCustomer);

        var category = new Category("Jogos", Optional.empty());
        manager.persist(category);

        Map<String, String> specifications = new HashMap<>();
        specifications.put("Mídia", "Digital");
        specifications.put("Plataforma", "PS4");
        specifications.put("Lançamento", "2018-04-20");

        product = new Product("God of War", new BigDecimal(80), 10, specifications, "Garoto", category, ownerUser);
        manager.persist(product);

        product.addImage("/image-1234.png");
        product.addImage("/image-1235.png");

        var firstQuestion = new ProductQuestion("Como libertar as valquírias?", firstCustomer, product);
        var secondQuestion = new ProductQuestion("Como chegar na montanha?", secondCustomer, product);

        manager.persist(firstQuestion);
        manager.persist(secondQuestion);

        var firstOpinion = new ProductOpinion("Achei bom", "Jogo da hora, jogo bem feito", 5, firstCustomer, product);
        var secondOpinion = new ProductOpinion("Não gostei", "Sei lá", 1, secondCustomer, product);

        manager.persist(firstOpinion);
        manager.persist(secondOpinion);
    }

    @Test
    @Transactional
    public void getRatings__should_return_average_and_total() {
        ProductRatingsProjection ratings = productRepository.getRatings(product.getId());

        assertEquals(3, ratings.getAverageRatings());
        assertEquals(2, ratings.getTotalRatings());
    }

    @Test
    @Transactional
    public void getProductInfo__should_return_info() {

        List<LastQuestionsProjection> lastQuestions = productQuestionRepository.findByProduct_IdOrderByCreatedAtDesc(product.getId(), PageRequest.of(0, 10));
        List<LastOpinionsProjection> lastOpinions = productOpinionRepository.findByProduct_IdOrderByCreatedAtDesc(product.getId(), PageRequest.of(0, 10));

        ProductInfoDTO info = productRepository.getProductInfo(product.getId(), lastQuestions, lastOpinions);

        assertEquals("God of War", info.getName());
        assertEquals("Garoto", info.getDescription());
        assertEquals(new BigDecimal(80), info.getPrice());
        assertEquals(10, info.getQuantity());

        assertEquals("Digital", info.getSpecifications().get("Mídia"));
        assertEquals("PS4", info.getSpecifications().get("Plataforma"));
        assertEquals("2018-04-20", info.getSpecifications().get("Lançamento"));

        assertThat(info.getImages(), containsInAnyOrder("/image-1234.png", "/image-1235.png"));

        assertEquals(3, info.getAverageRatings());
        assertEquals(2, info.getTotalRatings());

        List<String> questionsTitles = info.getLastQuestions().stream().map(LastQuestionsProjection::getTitle).collect(toList());
        assertThat(questionsTitles, contains("Como chegar na montanha?", "Como libertar as valquírias?"));

        List<String> opinionsTitles = info.getLastOpinions().stream().map(LastOpinionsProjection::getTitle).collect(toList());
        assertThat(opinionsTitles, contains("Não gostei", "Achei bom"));
    }

}