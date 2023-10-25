package com.rafaelwassoaski.sindicato.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentTest {

    @Test
    public void shouldCreateDocumentNameAndType(){
        DocumentType docType = new DocumentType("DocType Name");
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        User user = new User("Name1", "Password", "email@email.com", address, "000.000.000-00");
        Document doc = new Document("Name", docType, user, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertEquals("Name", doc.getName());
        Assertions.assertEquals(docType.getName(), doc.getDocumentType().getName());
        Assertions.assertEquals(user.getName(), doc.getDocumentUser().getName());
        Assertions.assertEquals(1000L, doc.getDocumentValue());
    }
}
