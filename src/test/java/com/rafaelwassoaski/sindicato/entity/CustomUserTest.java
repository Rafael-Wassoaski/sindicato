package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.enums.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
public class CustomUserTest {

    @Test
    public void shouldCreateUserWithNameAndPassword(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address,"000.000.000-00", Roles.USER);

        Assertions.assertEquals("Name1", customUser.getName());
        Assertions.assertEquals("Password", customUser.getPassword());
    }

    @Test
    public void shouldCreateUserWithADMRole(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address,"000.000.000-00", Roles.ADMIN);

        Assertions.assertEquals("Name1", customUser.getName());
        Assertions.assertEquals("Password", customUser.getPassword());
    }
}
