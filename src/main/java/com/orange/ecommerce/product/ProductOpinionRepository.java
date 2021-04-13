package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOpinionRepository extends CrudRepository<ProductOpinion, Long> {

    Optional<ProductOpinion> findByUserAndProduct(User user, Product product);
    List<LastOpinionsProjection> findByProduct_IdOrderByCreatedAtDesc(Long productId, Pageable pageable);
}
