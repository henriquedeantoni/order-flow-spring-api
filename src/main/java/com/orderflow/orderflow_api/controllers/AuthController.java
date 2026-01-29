package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.Role;
import com.orderflow.orderflow_api.models.Roles;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.repositories.RoleRepository;
import com.orderflow.orderflow_api.repositories.UserRepository;
import com.orderflow.orderflow_api.secutiry.jwt.JwtUtils;
import com.orderflow.orderflow_api.secutiry.request.LoginRequest;
import com.orderflow.orderflow_api.secutiry.request.SignupRequest;
import com.orderflow.orderflow_api.secutiry.response.MessageResponse;
import com.orderflow.orderflow_api.secutiry.response.UserInfoResponse;
import com.orderflow.orderflow_api.secutiry.services.UserDetailsImpl;
import com.orderflow.orderflow_api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticationUser(@RequestBody LoginRequest request){
        AuthenticationResult authenticationResult = authService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authenticationResult.getJwtResponseCookie().toString()).body(authenticationResult.getUserInfoResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request){
        return authService.register(request);
    }

    @GetMapping("/username")
    public String getUsername(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        } else {
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse userResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(Authentication authentication) {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Bye! You've been logged out successfully!"));
    }
}
