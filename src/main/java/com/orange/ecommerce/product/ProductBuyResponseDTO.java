package com.orange.ecommerce.product;

public class ProductBuyResponseDTO {

    private Long buyId;
    private String productName;
    private Integer quantity;
    private String buyerEmail;
    private String sellerEmail;
    private String redirectUrl;

    public ProductBuyResponseDTO(ProductBuy productBuy, String redirectUrl) {
        this.buyId = productBuy.getId();
        this.productName = productBuy.getProductName();
        this.quantity = productBuy.getQuantity();
        this.buyerEmail = productBuy.getBuyerEmail();
        this.sellerEmail = productBuy.getSellerEmail();
        this.redirectUrl = redirectUrl;
    }

    public Long getBuyId() {
        return buyId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
