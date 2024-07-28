package com.rafaelwassoaski.sindicato.service;


import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.enums.Roles;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.EmailAlreadyInUseException;
import com.rafaelwassoaski.sindicato.exceptions.EmptyPasswordException;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.management.relation.Role;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
public class CustomUserServiceTest {

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
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

        CustomUser customUser = userService.createUser(userDTO);
        CustomUser databaseCustomUser = userRepository.getReferenceById(customUser.getId());

        Assertions.assertNotNull(databaseCustomUser);
    }

    @Test
    public void shouldNotCreateUserWithSameEmail() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

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
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

        CustomUser customUser = userService.createUser(userDTO);
        CustomUser databaseCustomUser = userService.findUserById(customUser.getId());

        Assertions.assertNotNull(databaseCustomUser);
    }

    @Test
    public void shouldGetUserByUsername() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

        CustomUser customUser = userService.createUser(userDTO);
        CustomUser databaseCustomUser = userService.findUserByEmail(customUser.getEmail());

        Assertions.assertNotNull(databaseCustomUser);
    }

    @Test
    public void shouldHaveUserRoleInCommonUser() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

        CustomUser customUser = userService.createUser(userDTO);
        CustomUser databaseCustomUser = userService.findUserByEmail(customUser.getEmail());

        Assertions.assertEquals(databaseCustomUser.getRole(), Roles.USER);
    }

    @Test
    public void shouldNotHaveAdminRoleInCommonUser() throws BaseException {
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Rafael", "12345678", "email@email.com", address, "343.626.280-37");

        CustomUser customUser = userService.createUser(userDTO);
        CustomUser databaseCustomUser = userService.findUserByEmail(customUser.getEmail());

        Assertions.assertNotEquals(databaseCustomUser.getRole(), Roles.ADMIN);

    }
}
