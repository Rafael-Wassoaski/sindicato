package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.User;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmailAlreadyInUseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.exceptions.UserNotFoundException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserEmailExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserIdExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserPasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDTO userDTO) throws BaseException {
        User user = new User(userDTO);

        ChainValidation userValidation = this.createUserValidation();

        userValidation.isValid(user);

        return userRepository.save(user);
    }

    private ChainValidation createUserValidation(){
        UserEmailExistenceValidation userEmailExistenceValidation = new UserEmailExistenceValidation(null, userRepository);
        UserPasswordValidation userPasswordValidation = new UserPasswordValidation(userEmailExistenceValidation);


        return userPasswordValidation;
    }

    public User findUserById(Long id) throws ResourceNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o usuário de ID: %d", id));
        }

        return optionalUser.get();
    }
}
