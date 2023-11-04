package com.rafaelwassoaski.sindicato.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CustomUserTest {

    @Test
    public void shouldCreateUserWithNameAndPassword(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address,"000.000.000-00");

        Assertions.assertEquals("Name1", customUser.getName());
        Assertions.assertEquals("Password", customUser.getPassword());
    }
}
