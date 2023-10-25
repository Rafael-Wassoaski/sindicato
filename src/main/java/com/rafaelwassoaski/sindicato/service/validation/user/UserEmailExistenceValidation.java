package com.rafaelwassoaski.sindicato.service.validation.user;

import com.rafaelwassoaski.sindicato.entity.User;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmailAlreadyInUseException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

import java.util.Optional;

public class UserEmailExistenceValidation extends ChainValidation<User> {

    private UserRepository userRepository;

    public UserEmailExistenceValidation(ChainValidation nextValidation, UserRepository userRepository) {
        super(nextValidation);
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(User objectToValidate) throws BaseException {
        Optional<User> optionalDatabaseUser = userRepository.findByEmail(objectToValidate.getEmail());

        if (optionalDatabaseUser.isPresent()) {
            throw new EmailAlreadyInUseException();
        }

        return this.callNext(objectToValidate);
    }


}
