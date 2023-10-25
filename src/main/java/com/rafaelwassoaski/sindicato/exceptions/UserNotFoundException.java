package com.rafaelwassoaski.sindicato.exceptions;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException(String message) {
        super(String.format("O usuario %s não foi localizado", message));
    }
}
