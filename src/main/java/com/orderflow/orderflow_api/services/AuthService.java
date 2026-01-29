package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.secutiry.request.LoginRequest;
import com.orderflow.orderflow_api.secutiry.request.SignupRequest;
import com.orderflow.orderflow_api.secutiry.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthenticationResult login(LoginRequest request);

    public ResponseEntity<MessageResponse> register(SignupRequest request);
}
