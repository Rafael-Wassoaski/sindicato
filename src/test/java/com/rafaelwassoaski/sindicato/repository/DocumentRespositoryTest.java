package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentRespositoryTest {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void shouldCreateDocumentInDatabase(){
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);

        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        User user = new User("Name1", "Password", "email@email.com", address, "000.000.000-00");
        User createdUser =  userRepository.save(user);

        Document doc = new Document("Name", createdDocType, createdUser, 1000L, LocalDateTime.now(), "No OBS");
        Document createdDoc = documentRepository.save(doc);
        Document databaseDoc = documentRepository.getReferenceById(createdDoc.getId());

        Assertions.assertNotNull(databaseDoc);
    }
}
