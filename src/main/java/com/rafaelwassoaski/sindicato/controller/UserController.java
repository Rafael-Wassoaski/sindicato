package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.TokenDTO;
import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.service.HttpServletService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private HttpServletService httpServletService;


    @GetMapping("/login")
    public String loginPage(Model model){
        UserDTO userDTO = new UserDTO();

        model.addAttribute("userDTO", userDTO);

        return "users/Login";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response){
        service.logout(request, response);
        httpServletService.removeTokenCookie(response);

        return this.loginPage(model);
    }

    @PostMapping("/authenticate")
    public String authenticate(UserDTO userDTO, HttpServletResponse response) throws ResourceNotFoundException {
        try {
            CustomUser customUser = this.service.login(userDTO);

            String token = jwtService.generateToken(customUser);
            response = httpServletService.setTokenCookie(response, token);

            return "redirect:/documents/myDocs/" + customUser.getId() + "?page=0";
        } catch (ResponseStatusException | ResourceNotFoundException e) {
            System.out.println(e);
            throw e;
        }
    }

    @PostMapping("/newUser")
    public String newUser(Model model, UserDTO userDTO) throws BaseException {
        try {
            CustomUser customUser = this.service.createUser(userDTO);
            return this.loginPage(model);
        } catch (ResponseStatusException | BaseException e) {
            throw e;
        }
    }

    @GetMapping("/create")
    public String crate(Model model) {
        UserDTO userDTO = new UserDTO();

        model.addAttribute("userDTO", userDTO);

        return "users/Create";
    }
}
