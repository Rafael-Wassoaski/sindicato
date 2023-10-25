package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DocTypeRespositoryTest {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Test
    public void shouldCreateDocTypeInDatabase(){
        DocumentType docType = new DocumentType("DocType Name");
        DocumentType createdDocType = documentTypeRepository.save(docType);
        DocumentType databaseDocType = documentTypeRepository.getReferenceById(createdDocType.getId());

        Assertions.assertNotNull(databaseDocType);
    }
}
