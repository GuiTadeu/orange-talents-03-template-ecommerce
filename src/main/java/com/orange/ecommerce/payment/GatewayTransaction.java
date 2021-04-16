package com.orange.ecommerce.payment;

import com.orange.ecommerce.product.ProductBuy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class GatewayTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProductBuy productBuy;

    @NotNull
    private Long paymentId;

    @NotNull
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();

    public GatewayTransaction() {

    }

    public GatewayTransaction(ProductBuy productBuy, @NotNull Long paymentId, @NotNull String status) {
        this.productBuy = productBuy;
        this.paymentId = paymentId;
        this.status = status;
    }

    boolean isGatewaySuccess(GatewayStatusChecker checker) {
        return checker.isGatewaySuccess(status);
    }

    boolean isGatewayError(GatewayStatusChecker checker) {
        return checker.isGatewayError(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductBuy getProductBuy() {
        return productBuy;
    }

    public Long getProductBuyId() {
        return productBuy.getId();
    }

    public PaymentStatus getBuyStatus() {
        return productBuy.getStatus();
    }

    public Long getBuyerId() {
        return productBuy.getBuyerId();
    }

    public Long getSellerId() {
        return productBuy.getSellerId();
    }

    public PaymentMethod getPaymentMethod() {
        return productBuy.getPaymentMethod();
    }

    public String getPaymentMethodName() {
        return productBuy.getPaymentMethodName();
    }

    public String getPaymentMethodUrl() {
        return productBuy.getPaymentMethodUrl();
    }

    public String getPaymentMethodCode() {
        return productBuy.getPaymentMethodCode();
    }

    public String getProductName() {
        return productBuy.getProductName();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws Exception {
        if(this.status.equals("SUCCESS")) throw new Exception("Changing a successful transaction is not allowed!");
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public GatewayTransaction toNewTransaction(String status) {
        return new GatewayTransaction(productBuy, paymentId, status);
    }
}
