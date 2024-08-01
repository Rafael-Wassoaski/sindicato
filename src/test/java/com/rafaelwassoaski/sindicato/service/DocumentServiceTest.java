package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.enums.Roles;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.DocTypeNotFoundException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.exceptions.UserNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void beforeEach(){
        documentRepository.deleteAll();
        documentTypeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldCreateDocumentFromDocumentDTO() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        Document databaseDocument = documentRepository.getReferenceById(document.getId());

        Assertions.assertNotNull(databaseDocument);
        Assertions.assertEquals(documentDTO.getName(), document.getName());
    }

    @Test
    public void shouldNotDocumentWithNotExistentUser(){
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser(1L, "Name1", "Password", "email@email.com", address, "000.000.000-00", new HashSet<>(), Roles.USER);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), customUser, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertThrows(UserNotFoundException.class, () -> documentService.createDocument(documentDTO));
    }

    @Test
    public void shouldNotCreateDocumentWithNotExistentDocType() throws UserNotFoundException {
        DocumentType docType = new DocumentType(1L, "DocType Name",new HashSet<>());

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", docType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertThrows(DocTypeNotFoundException.class, () -> documentService.createDocument(documentDTO));
    }

    @Test
    public void shouldReturnDocumentbyId() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        Document createdDocument = documentService.findDocumentById(document.getId());

        Assertions.assertNotNull(createdDocument);
        Assertions.assertEquals(documentDTO.getName(), createdDocument.getName());

    }

    @Test
    public void shouldNotReturnDocumentbyId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> documentService.findDocumentById(12L));
    }

    @Test
    public void shouldReturnAllDocumentsbyUser() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);
        Document document2 = documentService.createDocument(documentDTO);

        Page<Document> createdDocuments = documentService.findDocumentUserId(customUser.getId(), 0);

        Assertions.assertNotNull(createdDocuments);
        Assertions.assertEquals(2, createdDocuments.getTotalElements());
    }

    @Test
    public void shouldUpdateOneDocument() throws BaseException {
        String updatedDocName = "New updatedName";
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        documentDTO.setName(updatedDocName);

        Document updatedDocument = documentService.updateDocument(document.getId(), documentDTO);

        Assertions.assertEquals(updatedDocument.getName(), updatedDocName);
        Assertions.assertEquals(updatedDocument.getId(), document.getId());
    }

    @Test
    public void shouldDeleteOneDocument() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        documentService.deleteDocument(document.getId());

        Optional<Document> deletedOptionalDocument = documentRepository.findById(document.getId());

        Assertions.assertFalse(deletedOptionalDocument.isPresent());
    }

    @Test
    public void shouldNotDeleteAUnexistentDocument() throws BaseException {
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> documentService.deleteDocument(1L));
    }

    @Test
    public void shouldSearchDocumentByName() throws BaseException {
        String DocName = "Doc Name";

        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO(DocName, createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);
        System.out.println(document.getName());

        Page<Document> documentsWithName = documentService.searchDocuments(DocName, 0);

        Assertions.assertEquals(documentsWithName.getTotalElements(), 1);
    }

    @Test
    public void shouldSearchDocumentByPartialName() throws BaseException {
        String DocName = "Doc Name";
        String partialDocName = "Name";

        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO(DocName, createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);
        System.out.println(document.getName());

        Page<Document> documentsWithName = documentService.searchDocuments(partialDocName, 0);

        Assertions.assertEquals(documentsWithName.getTotalElements(), 1);
    }

    @Test
    public void shouldSearchDocumentByCreationDate() throws BaseException {

        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Doc Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);
        System.out.println(LocalDate.now().toString());
        System.out.println(document.getCreatedAt().toString());

        Page<Document> documentsWithName = documentService.searchDocuments(LocalDate.now().toString(), 0);

        Assertions.assertEquals(documentsWithName.getTotalElements(), 1);
    }

    @Test
    public void shouldSearchDocumentByDocumentValue() throws BaseException {
        Long documentValue = 1000L;
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Doc Name", createdDocType.getId(), createdCustomUser, documentValue, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        Page<Document> documentsWithName = documentService.searchDocuments(documentValue.toString(), 0);

        Assertions.assertEquals(documentsWithName.getTotalElements(), 1);
    }

    @Test
    public void shouldSearchDocumentByObs() throws BaseException {
        String obs = "No OBS";
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", Roles.USER);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Doc Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), obs);
        Document document = documentService.createDocument(documentDTO);

        Page<Document> documentsWithName = documentService.searchDocuments(obs, 0);

        Assertions.assertEquals(documentsWithName.getTotalElements(), 1);
    }
}
