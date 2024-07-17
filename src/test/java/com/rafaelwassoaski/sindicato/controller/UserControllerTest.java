package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    String baseEmail = "email@email.com";


    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    public void shouldCreateUserWhenAllFieldsAreCorrect() throws Exception {
        MockHttpServletResponse response = this.createUserWithEmail(baseEmail);

        Optional<CustomUser> user = userRepository.findByEmail(baseEmail);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(user.isPresent());
    }

    @Test
    public void shouldLoginWithCreatedUser() throws Exception {
        this.createUserWithEmail(baseEmail);
        MockHttpServletResponse response = mockMvc.perform(post("/users/authenticate")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("email", baseEmail)
                        .param("password", "12345678"))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.FOUND.value(), response.getStatus());
    }

    @Test
    public void shouldNotLoginWithWrongPassword() throws Exception {
        this.createUserWithEmail(baseEmail);
        MockHttpServletResponse response = mockMvc.perform(post("/users/authenticate")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("email", baseEmail)
                        .param("password", "wrong_password"))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.MOVED_TEMPORARILY.value(), response.getStatus());
    }

    @Test
    public void shouldNotLoginWithoutPassword() throws Exception {
        this.createUserWithEmail(baseEmail);
        MockHttpServletResponse response = mockMvc.perform(post("/users/authenticate")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("email", baseEmail))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.MOVED_TEMPORARILY.value(), response.getStatus());
    }

    @Test
    public void shouldRedirectUserToLoginPageWhenUserIsNotLoggedIn() throws Exception {
        String redirectUrl = mockMvc.perform(get("/documents/allDocuments")).andReturn().getResponse().getRedirectedUrl();
        Assertions.assertTrue(redirectUrl.contains("/users/login"));
    }

    private MockHttpServletResponse createUserWithEmail(String email) throws Exception {
        return mockMvc.perform(post("/users/newUser")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("cpf", "570.035.140-44")
                        .param("email", email)
                        .param("password", "12345678")
                        .param("confirm-password", "12345678")
                        .param("address.street", "Rua a")
                        .param("address.houseNumber", "123")
                        .param("address.city", "Canoinhas")
                        .param("address.state", "SC")
                        .param("address.country", "Brazil"))
                .andReturn().getResponse();
    }
}
