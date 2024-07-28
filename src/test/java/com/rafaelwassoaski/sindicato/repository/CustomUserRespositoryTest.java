package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.enums.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
public class CustomUserRespositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateUserInDatabase(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);
        CustomUser databaseCustomUser = userRepository.getReferenceById(createdCustomUser.getId());

        Assertions.assertNotNull(databaseCustomUser);
    }
}
