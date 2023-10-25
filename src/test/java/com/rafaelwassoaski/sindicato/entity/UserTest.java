package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserTest {

    @Test
    public void shouldCreateUserWithNameAndPassword(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        User user = new User("Name1", "Password", "email@email.com", address,"000.000.000-00");

        Assertions.assertEquals("Name1", user.getName());
        Assertions.assertEquals("Password", user.getPassword());
    }
}
