package com.rafaelwassoaski.sindicato.service.validation;

import com.rafaelwassoaski.sindicato.exceptions.BaseException;

public abstract class ChainValidation<P> {

    private ChainValidation nextValidation;

    public ChainValidation(ChainValidation nextValidation) {
        this.nextValidation = nextValidation;
    }

    public ChainValidation() {}

    public abstract boolean isValid(P objectToValidate) throws BaseException;

    protected boolean callNext(P objectToValidate) throws BaseException {
        if(this.nextValidation == null){
            return true;
        }

        return this.nextValidation.isValid(objectToValidate);
    }

}
