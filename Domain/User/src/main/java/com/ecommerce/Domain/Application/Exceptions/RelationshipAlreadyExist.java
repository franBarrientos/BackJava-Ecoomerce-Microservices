package com.ecommerce.Domain.Application.Exceptions;

public class RelationshipAlreadyExist extends RuntimeException{
    public RelationshipAlreadyExist(String msg){
        super(msg);
    }
}
