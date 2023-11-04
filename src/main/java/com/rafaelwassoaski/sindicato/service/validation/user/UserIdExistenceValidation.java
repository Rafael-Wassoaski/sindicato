package com.rafaelwassoaski.sindicato.service.validation.user;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.UserNotFoundException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

import java.util.Optional;

public class UserIdExistenceValidation extends ChainValidation<CustomUser> {

    private UserRepository userRepository;

    public UserIdExistenceValidation(ChainValidation nextValidation, UserRepository userRepository) {
        super(nextValidation);
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(CustomUser objectToValidate) throws BaseException {
        Optional<CustomUser> optionalDatabaseUser = userRepository.findById(objectToValidate.getId());

        if (!optionalDatabaseUser.isPresent()) {
            throw new UserNotFoundException(objectToValidate.getName());
        }

        return this.callNext(objectToValidate);
    }


}
