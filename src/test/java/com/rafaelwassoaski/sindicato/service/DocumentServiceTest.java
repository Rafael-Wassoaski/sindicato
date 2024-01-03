package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

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
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
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
        CustomUser customUser = new CustomUser(1L, "Name1", "Password", "email@email.com", address, "000.000.000-00", new HashSet<>(), false);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), customUser, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertThrows(UserNotFoundException.class, () -> documentService.createDocument(documentDTO));
    }

    @Test
    public void shouldNotCreateDocumentWithNotExistentDocType() throws UserNotFoundException {
        DocumentType docType = new DocumentType(1L, "DocType Name",new HashSet<>());

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", docType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertThrows(DocTypeNotFoundException.class, () -> documentService.createDocument(documentDTO));
    }

    @Test
    public void shouldReturnDocumentbyId() throws BaseException {
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
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
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);
        Document document2 = documentService.createDocument(documentDTO);

        List<Document> createdDocuments = documentService.findDocumentUserId(customUser.getId());

        Assertions.assertNotNull(createdDocuments);
        Assertions.assertEquals(2, createdDocuments.size());
    }

    @Test
    public void shouldUpdateOneDocument() throws BaseException {
        String updatedDocName = "New updatedName";
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
        CustomUser createdCustomUser =  userRepository.save(customUser);

        DocumentDTO documentDTO = new DocumentDTO("Name", createdDocType.getId(), createdCustomUser, 1000L, LocalDateTime.now(), "No OBS");
        Document document = documentService.createDocument(documentDTO);

        documentDTO.setName(updatedDocName);

        Document updatedDocument = documentService.updateDocument(document.getId(), documentDTO);

        Assertions.assertEquals(updatedDocument.getName(), updatedDocName);
        Assertions.assertEquals(updatedDocument.getId(), document.getId());
    }
}
