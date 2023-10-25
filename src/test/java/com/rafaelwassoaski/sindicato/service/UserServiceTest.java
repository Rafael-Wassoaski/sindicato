package com.rafaelwassoaski.sindicato.service;


import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.User;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmailAlreadyInUseException;
import com.rafaelwassoaski.sindicato.exceptions.EmptyPasswordException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach(){
        userRepository.deleteAll();
    }

    @Test
    public void shouldCreateUserFromUserDTO() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "000.000.000-00");

        User user = userService.createUser(userDTO);
        User databaseUser = userRepository.getReferenceById(user.getId());

        Assertions.assertNotNull(databaseUser);
    }

    @Test
    public void shouldNotCreateUserWithSameEmail() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "000.000.000-00");

        userService.createUser(userDTO);

        Assertions.assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(userDTO));
    }

    @Test
    public void shouldNotCreateUserWithEmptyPassword() throws EmailAlreadyInUseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "", "email@email.com", address, "000.000.000-00");

        Assertions.assertThrows(EmptyPasswordException.class, () -> userService.createUser(userDTO));
    }

    @Test
    public void shouldGetUserById() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "000.000.000-00");

        User user = userService.createUser(userDTO);
        User databaseUser = userService.findUserById(user.getId());

        Assertions.assertNotNull(databaseUser);
    }
}
