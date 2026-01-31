package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.security.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class AuthenticationResult {
    private final UserInfoResponse userInfoResponse;

    private final ResponseCookie jwtResponseCookie;
}
