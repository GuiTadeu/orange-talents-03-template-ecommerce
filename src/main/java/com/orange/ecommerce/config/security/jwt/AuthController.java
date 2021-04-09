package com.orange.ecommerce.config.security.jwt;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;

    public AuthController(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping
    public ResponseEntity<AuthTokenOutput> authenticate(@RequestBody UserLoginForm loginForm) {

        UsernamePasswordAuthenticationToken authenticationToken = loginForm.build();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String jwt = tokenManager.generateToken(authentication);
        var tokenResponse = new AuthTokenOutput("Bearer", jwt);

        return ResponseEntity.ok(tokenResponse);
    }
}
