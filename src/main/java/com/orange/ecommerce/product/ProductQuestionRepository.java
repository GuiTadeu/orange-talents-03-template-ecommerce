package com.orange.ecommerce.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductQuestionRepository extends CrudRepository<ProductQuestion, Long> {

    List<LastQuestionsProjection> findByProduct_IdOrderByCreatedAtDesc(Long productId, Pageable pageable);
}
