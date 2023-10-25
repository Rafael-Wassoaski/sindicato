package com.rafaelwassoaski.sindicato.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentTypeTest {

    @Test
    public void shouldCreateDocumentTypeWithName(){
        DocumentType docType = new DocumentType("DocType Name");

        Assertions.assertEquals("DocType Name", docType.getName());
    }
}
