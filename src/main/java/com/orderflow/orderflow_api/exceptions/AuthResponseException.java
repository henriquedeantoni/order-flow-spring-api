package com.orderflow.orderflow_api.exceptions;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

public class AuthResponseException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public AuthResponseException(String msg) {
        super(msg);
    }
}
