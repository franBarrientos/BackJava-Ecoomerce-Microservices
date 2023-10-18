package com.ecommerce.Domain.Application.Exceptions;



public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg){
        super(msg);
    }
}
