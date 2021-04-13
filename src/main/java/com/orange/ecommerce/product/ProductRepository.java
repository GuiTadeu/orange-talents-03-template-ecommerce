package com.orange.ecommerce.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product getByName(String name);

    @Query("SELECT COUNT(prodOpinion.rating) as totalRatings,"
            + " AVG(prodOpinion.rating) as averageRatings"
            + " FROM ProductOpinion prodOpinion"
            + " WHERE prodOpinion.product.id = :productId")
    ProductRatingsProjection getRatings(@Param("productId") Long productId);

    default ProductInfoDTO getProductInfo(Long productId, List<LastQuestionsProjection> lastQuestions, List<LastOpinionsProjection> lastOpinions) {
        Product product = getOne(productId);
        ProductRatingsProjection ratings = getRatings(productId);

        return new ProductInfoDTO(product, ratings, lastQuestions, lastOpinions);
    }
}
