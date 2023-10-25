package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.User;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.DocumentService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;
    public Document shouldCreateDocumentFromDocumentDTO() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        User user = new User("Name1", "Password", "email@email.com", address, "000.000.000-00");
        User createdUser =  userRepository.save(user);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType, createdUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = service.createDocument(documentDTO);

        Document databaseDocument = documentRepository.getReferenceById(document.getId());
        return databaseDocument;
    }

    @GetMapping
    public String getDocuments(Model model) throws BaseException {
        Document document = shouldCreateDocumentFromDocumentDTO();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if(!(authentication instanceof AnonymousAuthenticationToken)){
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//            isOwner = service.isOwner(article, userDetails);
//        }

        System.out.println(document.getName());
        model.addAttribute("documents", Arrays.asList(document));

        return "documents/Documents";
    }

}