package com.orange.ecommerce.payment;

public interface GatewayStatusChecker {

    boolean isGatewaySuccess(String gatewayStatus);
    boolean isGatewayError(String gatewayStatus);
}
