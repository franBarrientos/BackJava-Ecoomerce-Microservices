package com.ecommerce.Domain.Application.Exceptions;

public class BadRequest extends RuntimeException {
    public BadRequest(String msg){
        super(msg);
    }
}
