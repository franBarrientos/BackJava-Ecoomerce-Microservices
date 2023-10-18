package com.ecommerce.Domain.Infrastructure.Rest.Config;

import org.springframework.http.ResponseEntity;

public class ApiResponse {

    private boolean ok;
    private Object message;
    private Object body;

    // Constructor
    public ApiResponse(boolean ok, Object message, Object body) {
        setOk(ok);
        setMessage(message);
        setBody(body);
    }

    public static ResponseEntity<ApiResponse> oK(Object body) {
        return ResponseEntity.ok(new ApiResponse(true, "success", body));
    }

    public static ResponseEntity<ApiResponse> notFound(Object body) {
        return ResponseEntity.status(404).body(new ApiResponse(false, body.toString(), null));
    }

    public static ResponseEntity<ApiResponse> serverError() {
        return ResponseEntity.status(500).body(new ApiResponse(false, "Uknown", null));
    }
    public static ResponseEntity<ApiResponse> serverError(Object body) {
        return ResponseEntity.status(500).body(new ApiResponse(false, body.toString(), null));
    }

    public static ResponseEntity<ApiResponse> badRequest(Object body) {
        return ResponseEntity.status(400).body(new ApiResponse(false, body, null));
    }
    public static ResponseEntity<ApiResponse> unathorized(Object body) {
        return ResponseEntity.status(401).body(new ApiResponse(false, body, null));
    }

    // Getters and setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
