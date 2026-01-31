package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.security.request.LoginRequest;
import com.orderflow.orderflow_api.security.request.SignupRequest;
import com.orderflow.orderflow_api.security.response.MessageResponse;
import com.orderflow.orderflow_api.security.response.UserInfoResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthenticationResult login(LoginRequest request);

    public ResponseEntity<MessageResponse> register(SignupRequest request);

    UserInfoResponse getUserDetails(Authentication authentication);

    ResponseCookie logOutUser();
}
