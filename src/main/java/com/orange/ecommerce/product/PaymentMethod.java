package com.orange.ecommerce.product;

public enum PaymentMethod {

    PAYPAL("PayPal", "https://paypal.com"),
    PAGSEGURO("PagSeguro", "https://pagseguro.com");

    private String name;
    private String url;

    PaymentMethod(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
