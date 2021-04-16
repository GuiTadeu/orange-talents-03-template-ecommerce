package com.orange.ecommerce.payment;

import com.orange.ecommerce.product.ProductBuy;

import javax.validation.constraints.NotNull;

public class GatewayReturn {

    @NotNull
    private Long paymentId;

    @NotNull
    private String status;

    public GatewayReturn(Long paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }

    public GatewayTransaction toModel(ProductBuy productBuy) {
        return new GatewayTransaction(productBuy, paymentId, status);
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setGatewayPaymentId(Long gatewayPaymentId) {
        this.paymentId = gatewayPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
