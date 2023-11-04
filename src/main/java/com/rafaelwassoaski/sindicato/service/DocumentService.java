package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;
import com.rafaelwassoaski.sindicato.service.validation.docType.user.DocumentTypeExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserIdExistenceValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private UserService userService;


    public Document createDocument(DocumentDTO documentDTO) throws BaseException {
        Document documentFromDTO = new Document(documentDTO);
        CustomUser dtoCustomUser = documentDTO.getDocumentUser();
        DocumentType dtoDocType = documentDTO.getDocumentType();

        ChainValidation userValidation = this.createUserValidation();
        ChainValidation docTypeValidation = this.createDocTypeValidation();

        userValidation.isValid(dtoCustomUser);
        docTypeValidation.isValid(dtoDocType);


        return documentRepository.save(documentFromDTO);
    }

    private ChainValidation createUserValidation(){
        UserIdExistenceValidation userExistenceValidation = new UserIdExistenceValidation(null, userRepository);

        return userExistenceValidation;
    }

    private ChainValidation createDocTypeValidation(){
        DocumentTypeExistenceValidation docTypeExistenceValidation = new DocumentTypeExistenceValidation(null, documentTypeRepository);

        return docTypeExistenceValidation;
    }

    public Document findDocumentById(Long id) throws ResourceNotFoundException {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if(!optionalDocument.isPresent()){
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o documento de ID: %d", id));
        }

        return optionalDocument.get();
    }

    public List<Document> findDocumentUserId(Long id) throws ResourceNotFoundException {
        CustomUser customUser = userService.findUserById(id);
        List<Document> userDocuments = documentRepository.findAllByDocumentCustomUser(customUser);
        return userDocuments;
    }
}
