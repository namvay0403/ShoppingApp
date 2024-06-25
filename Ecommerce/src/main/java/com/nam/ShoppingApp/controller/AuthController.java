package com.nam.ShoppingApp.controller;

import com.nam.ShoppingApp.dto.AuthenticationRequest;
import com.nam.ShoppingApp.dto.SignupRequest;
import com.nam.ShoppingApp.entity.User;
import com.nam.ShoppingApp.repository.UserRepository;
import com.nam.ShoppingApp.services.auth.AuthService;
import com.nam.ShoppingApp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthService authService;

  @PostMapping("/authenticate")
  public void createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response)
      throws JSONException, IOException {
    log.info("Authenticating user: {}", authenticationRequest.getUsername());
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Incorrect username or password");
    }
    var userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
    String jwt = jwtUtil.generateToken(userDetails.getUsername());

    if (optionalUser.isPresent()) {
      response
          .getWriter()
          .write(
              new JSONObject()
                  .put("userId", optionalUser.get().getId())
                  .put("role", optionalUser.get().getRole())
                  .put("token", jwt)
                  .toString());
      response.addHeader("Access-Control-Expose-Headers", "Authorization");
      response.addHeader(
          "Access-Control-Allow-Headers",
          "Authorization, X-PINGOTHER, Origin, "
              + "X-Requested-With, Content-Type, Accept, X-Custom-header");
      response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
    if (authService.hasUserWithEmail(request.getEmail())) {
      return ResponseEntity.badRequest().body("User with this email already exists");
    }

    return ResponseEntity.ok(authService.createUser(request));
  }
}
