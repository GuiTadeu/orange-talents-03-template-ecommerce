package com.orange.ecommerce.payment;

public class PagseguroGeneralStatusChecker implements GatewayStatusChecker {

    @Override
    public boolean isGatewaySuccess(String gatewayStatus) {
        return "SUCCESS".equals(gatewayStatus);
    }

    @Override
    public boolean isGatewayError(String gatewayStatus) {
        return "ERROR".equals(gatewayStatus);
    }
}
