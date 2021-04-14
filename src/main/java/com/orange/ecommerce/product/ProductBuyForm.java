package com.orange.ecommerce.product;

import com.orange.ecommerce.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductBuyForm {

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    private PaymentMethod paymentMethod;

    public ProductBuy toModel(Product product, User buyer, User seller) {
        return new ProductBuy(product, buyer, seller, paymentMethod, PaymentStatus.STARTED, quantity);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
