package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.enums.Roles;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.DocumentService;
import com.rafaelwassoaski.sindicato.service.UserService;
import com.rafaelwassoaski.sindicato.service.secutiry.JWTAuthFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DocumentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @SpyBean
    private UserService userService;

    @Test
    public void shouldDeleteDocumentIsUserIsDocumentOwner() throws Exception {

        doNothing().when(userService).sameUserOrAdmin(any(), any());
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        String password = "Password";
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        UserDTO userDTO = new UserDTO("Name1", password, "email@email.com", address, "009.742.370-00");
        CustomUser createdCustomUser =  userService.createUser(userDTO);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        mockMvc.perform(delete(String.format("/documents/myDocs/%s/%s/deleteDoc", createdCustomUser.getId(), document.getId())))
                .andReturn().getResponse();

        Optional<Document> deletedOptionalDocument = documentRepository.findById(document.getId());

        Assertions.assertFalse(deletedOptionalDocument.isPresent());
    }
}
