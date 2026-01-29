package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.secutiry.request.LoginRequest;

public interface AuthService {
    AuthenticationResult login(LoginRequest request);
}
