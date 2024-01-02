package com.rafaelwassoaski.sindicato.service.validation.user;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmptyPasswordException;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

public class UserPasswordValidation extends ChainValidation<CustomUser> {

    public UserPasswordValidation(ChainValidation nextValidation) {
        super(nextValidation);
    }

    @Override
    public boolean isValid(CustomUser objectToValidate) throws BaseException {
        System.out.println("AAAA " + objectToValidate.getPassword());

        if (objectToValidate.getPassword() == null || objectToValidate.getPassword().isEmpty()) {
            System.out.println("AAAA ENTREI");
            throw new EmptyPasswordException();
        }

        return this.callNext(objectToValidate);
    }


}
