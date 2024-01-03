package com.rafaelwassoaski.sindicato.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentTest {

    @Test
    public void shouldCreateDocumentNameAndType(){
        DocumentType docType = new DocumentType("DocType Name");
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");
        CustomUser customUser = new CustomUser("Name1", "Password", "email@email.com", address, "000.000.000-00", false);
        Document doc = new Document("Name", docType, customUser, 1000L, LocalDateTime.now(), "No OBS");

        Assertions.assertEquals("Name", doc.getName());
        Assertions.assertEquals(docType.getName(), doc.getDocumentType().getName());
        Assertions.assertEquals(customUser.getName(), doc.getDocumentCustomUser().getName());
        Assertions.assertEquals(1000L, doc.getDocumentValue());
    }
}
