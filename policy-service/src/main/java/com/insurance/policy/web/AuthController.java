// src/main/java/com/insurance/policy/web/AuthController.java
package com.insurance.policy.web;

import com.insurance.policy.security.JwtService;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final JwtService jwtService;
  private final ProviderManager authManager;

  public AuthController(JwtService jwtService, UserDetailsService uds, PasswordEncoder encoder) {
    this.jwtService = jwtService;
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(uds);
    provider.setPasswordEncoder(encoder);
    this.authManager = new ProviderManager(provider);
  }

  @PostMapping("/token")
  public Map<String, String> token(@RequestBody Map<String, String> req) {
    String username = req.get("username");
    String password = req.get("password");

    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );

    var roles = auth.getAuthorities().stream()
        .map(a -> a.getAuthority().replace("ROLE_", "")) // store without prefix
        .collect(Collectors.toList());

    String jwt = jwtService.generate(username, roles);
    return Map.of("access_token", jwt, "token_type", "Bearer");
  }
}
