package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.secutiry.JWTService;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserCPFValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserEmailExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserPasswordValidation;
import com.rafaelwassoaski.sindicato.util.CookiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder userEncoder;
    @Autowired
    private JWTService jwtService;

    public CustomUser createUser(UserDTO userDTO) throws BaseException {
        CustomUser customUser = new CustomUser(userDTO);
        ChainValidation userValidation = this.createUserValidation();

        userValidation.isValid(customUser);

        CustomUser encodeCustomUser = this.encodeUserData(userDTO);

        return userRepository.save(encodeCustomUser);
    }

    private ChainValidation createUserValidation(){
        UserCPFValidation userCPFValidation = new UserCPFValidation(null);
        UserEmailExistenceValidation userEmailExistenceValidation = new UserEmailExistenceValidation(userCPFValidation, userRepository);
        UserPasswordValidation userPasswordValidation = new UserPasswordValidation(userEmailExistenceValidation);

        return userPasswordValidation;
    }

    private CustomUser encodeUserData(UserDTO userDTO){
        String encodedPassword = userEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        return new CustomUser(userDTO);
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
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o usuário com e-mail: %s", email));
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

    public void sameUserOrAdmin(Long id, HttpServletRequest request) throws ResourceNotFoundException {
        this.userIsAdmin(request);
        this.sameUser(id, request);
    }

    public void userIsAdmin(HttpServletRequest request) throws ResourceNotFoundException {
        String token = this.getToken(request);
        String username = jwtService.getUsername(token);
        CustomUser customUser = this.findUserByEmail(username);

        if(!customUser.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void sameUser(Long id, HttpServletRequest request) throws ResourceNotFoundException {
        CustomUser customUser = this.findUserById(id);

        String token = this.getToken(request);
        String username = jwtService.getUsername(token);

        if(!username.equals(customUser.getEmail())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private String getToken(HttpServletRequest request){
        Optional<String> optionalToken = CookiesUtils.extractTokenCookie(request);

        if(!optionalToken.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optionalToken.get();
    }

    public CustomUser findUserByCookie(HttpServletRequest request) {
        String token = CookiesUtils.extractTokenCookie(request).get();
        String email = jwtService.getUsername(token);

        Optional<CustomUser> optionalCustomUser = this.userRepository.findByEmail(email);

        if(!optionalCustomUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optionalCustomUser.get();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

    }
}
