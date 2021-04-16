package com.orange.ecommerce.product;

import com.orange.ecommerce.payment.PaymentMethod;
import com.orange.ecommerce.payment.PaymentStatus;
import com.orange.ecommerce.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.orange.ecommerce.payment.PaymentStatus.COMPLETED;

@Entity
public class ProductBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    @ManyToOne
    private User buyer;

    @NotNull
    @ManyToOne
    private User seller;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @NotNull
    @Positive
    private Integer quantity;

    public ProductBuy() {

    }

    public ProductBuy(@NotNull Product product, @NotNull User buyer,
                      @NotNull User seller, @NotNull PaymentMethod paymentMethod,
                      @NotNull PaymentStatus status, @NotNull @Positive Integer quantity) {
        this.product = product;
        this.buyer = buyer;
        this.seller = seller;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return product.getName();
    }

    public User getBuyer() {
        return buyer;
    }

    public Long getBuyerId() {
        return buyer.getId();
    }

    public String getBuyerEmail() {
        return buyer.getEmail();
    }

    public Long getSellerId() {
        return seller.getId();
    }

    public String getSellerEmail() {
        return seller.getEmail();
    }

    public User getSeller() {
        return seller;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentMethodUrl() {
        return paymentMethod.getUrl();
    }

    public String getPaymentMethodName() {
        return paymentMethod.getName();
    }

    public String getPaymentMethodCode() {
        return paymentMethod.getCode();
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) throws Exception {
        if(COMPLETED.equals(status))
            throw new Exception("Changing a completed buy is not allowed!");
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
