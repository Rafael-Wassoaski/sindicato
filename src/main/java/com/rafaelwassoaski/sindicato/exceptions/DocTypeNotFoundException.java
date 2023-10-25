package com.rafaelwassoaski.sindicato.exceptions;

public class DocTypeNotFoundException extends BaseException{
    public DocTypeNotFoundException(String message) {
        super(String.format("O tipo de documento %s não foi localizado", message));
    }
}
