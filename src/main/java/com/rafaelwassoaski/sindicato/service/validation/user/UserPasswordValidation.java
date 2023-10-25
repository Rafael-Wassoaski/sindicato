package com.rafaelwassoaski.sindicato.service.validation.user;

import com.rafaelwassoaski.sindicato.entity.User;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmptyPasswordException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

import java.util.Optional;

public class UserPasswordValidation extends ChainValidation<User> {

    public UserPasswordValidation(ChainValidation nextValidation) {
        super(nextValidation);
    }

    @Override
    public boolean isValid(User objectToValidate) throws BaseException {
        if (objectToValidate.getPassword() == null || objectToValidate.getPassword().isEmpty()) {
            throw new EmptyPasswordException();
        }

        return this.callNext(objectToValidate);
    }


}
