package com.rafaelwassoaski.sindicato.exceptions;

public class EmailAlreadyInUseException extends BaseException{
    public EmailAlreadyInUseException() {
        super(String.format("O email cadastrado já está em uso"));
    }
}
