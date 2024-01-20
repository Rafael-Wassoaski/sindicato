package com.rafaelwassoaski.sindicato.exceptions;

public class InvalidCPFException extends BaseException{
    public InvalidCPFException() {
        super(String.format("O cpf informado é inválido"));
    }
}
