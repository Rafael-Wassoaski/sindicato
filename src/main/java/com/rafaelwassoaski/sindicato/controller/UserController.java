package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.TokenDTO;
import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.service.UserService;
import com.rafaelwassoaski.sindicato.service.secutiry.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;

    @GetMapping("/login")
    public String loginPage(Model model){
        UserDTO userDTO = new UserDTO();

        model.addAttribute("userDTO", userDTO);

        return "users/Login";
    }

    @PostMapping("/authenticate")
    public TokenDTO authenticate(UserDTO userDTO) {
        try {
            CustomUser customUser = this.service.login(userDTO);

            String token = jwtService.generateToken(customUser);

            return new TokenDTO(customUser.getEmail(), token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
