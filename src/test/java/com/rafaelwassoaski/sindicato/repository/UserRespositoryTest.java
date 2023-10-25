package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserRespositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateUserInDatabase(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        User user = new User("Name1", "Password", "email@email.com", address, "000.000.000-00");
        User createdUser =  userRepository.save(user);
        User databaseUser = userRepository.getReferenceById(createdUser.getId());

        Assertions.assertNotNull(databaseUser);
    }
}
