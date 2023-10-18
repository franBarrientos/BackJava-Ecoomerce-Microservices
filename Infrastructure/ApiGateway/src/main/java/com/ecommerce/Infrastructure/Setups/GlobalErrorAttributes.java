package com.ecommerce.Infrastructure.Setups;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("ok", false);
        errorResponse.put("message", super.getError(request).getMessage());
        errorResponse.put("body", null);

        Map<String, Object> errorResponsess = super.getErrorAttributes(request, options);
        int status = HttpStatus.valueOf((Integer) errorResponsess.get("status")).value();

        errorResponse.put("status",status);
        return errorResponse;
    }
}

