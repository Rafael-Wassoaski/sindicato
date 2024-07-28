package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            CustomUser customUser = this.userService.findUserByEmail(email);

            return User.builder()
                    .username(customUser.getEmail())
                    .password(customUser.getPassword())
                    .roles(customUser.getUserRoleAsArray())
                    .build();

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
