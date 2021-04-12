package com.orange.ecommerce.product;

import java.util.List;

public class ProductImagesDTO {

    private Long productId;
    private List<String> imagesUrl;

    public ProductImagesDTO(Product product) {
        this.productId = product.getId();
        this.imagesUrl = product.getImagesUrl();
    }

    public Long getProductId() {
        return productId;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }
}
