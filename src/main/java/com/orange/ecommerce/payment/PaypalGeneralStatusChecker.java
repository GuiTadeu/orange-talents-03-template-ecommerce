package com.orange.ecommerce.payment;

public class PaypalGeneralStatusChecker implements GatewayStatusChecker {

    @Override
    public boolean isGatewaySuccess(String gatewayStatus) {
        return "1".equals(gatewayStatus);
    }

    @Override
    public boolean isGatewayError(String gatewayStatus) {
        return "0".equals(gatewayStatus);
    }
}
