package com.orange.ecommerce.config.security;

public class OpenURLS {

    public static final String[] VALUES = {
            "/auth",
            "/h2-console/**",
            "/users/**",
            "/products/*/info",
            "/gateway/pagseguro/productBuy/*/return",
            "/gateway/paypal/productBuy/*/return"
    };
}
