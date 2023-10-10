package com.ecommerce.Domain.Application.Exceptions;

public class Unathorized extends RuntimeException{
    public Unathorized(String message) {
        super(message);
    }
}
