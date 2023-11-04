package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserEmailExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserPasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder userEncoder;

    public CustomUser createUser(UserDTO userDTO) throws BaseException {
        CustomUser customUser = new CustomUser(userDTO);

        ChainValidation userValidation = this.createUserValidation();

        userValidation.isValid(customUser);

        return userRepository.save(customUser);
    }

    private ChainValidation createUserValidation(){
        UserEmailExistenceValidation userEmailExistenceValidation = new UserEmailExistenceValidation(null, userRepository);
        UserPasswordValidation userPasswordValidation = new UserPasswordValidation(userEmailExistenceValidation);


        return userPasswordValidation;
    }

    public CustomUser findUserById(Long id) throws ResourceNotFoundException {
        Optional<CustomUser> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o usuário de ID: %d", id));
        }

        return optionalUser.get();
    }

    public CustomUser findUserByEmail(String email) throws ResourceNotFoundException {
        Optional<CustomUser> optionalUser = userRepository.findByEmail(email);

        if(!optionalUser.isPresent()){
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o usuário com e-mail: %d", email));
        }

        return optionalUser.get();
    }

    public CustomUser login(UserDTO userDTO) throws ResourceNotFoundException {
        this.authenticate(userDTO);
        CustomUser customUser = this.findUserByEmail(userDTO.getEmail());

        return customUser;
    }

    private void authenticate(UserDTO userDTO) throws ResourceNotFoundException {
        CustomUser customUser = this.findUserByEmail(userDTO.getEmail());
        UserDetails userDetails = User.builder()
                .username(customUser.getEmail())
                .password(customUser.getPassword())
                .roles(new String[] {"ADMIN"})
                .build();

        boolean isSamePassword =
                this.userEncoder.matches(userDTO.getPassword(), userDetails.getPassword());

        if (!isSamePassword) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }
}
