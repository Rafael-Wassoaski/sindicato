package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00");
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType, createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
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