package com.rafaelwassoaski.sindicato.exceptions;

public class EmptyPasswordException extends BaseException{
    public EmptyPasswordException() {
        super(String.format("Senha não pode ser vazia"));
    }
}
