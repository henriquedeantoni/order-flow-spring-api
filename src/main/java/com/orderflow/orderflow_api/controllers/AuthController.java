package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.security.request.LoginRequest;
import com.orderflow.orderflow_api.security.request.SignupRequest;
import com.orderflow.orderflow_api.security.response.MessageResponse;
import com.orderflow.orderflow_api.security.response.UserInfoResponse;
import com.orderflow.orderflow_api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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
        if(authentication == null){
            return "";
        } else {
            return authentication.getName();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserInfoResponse userDetailsInfo = authService.getUserDetails(authentication);
        return ResponseEntity.ok().body(userDetailsInfo);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(Authentication authentication) {
        ResponseCookie cookie = authService.logOutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been logged out successfully!"));
    }
}
