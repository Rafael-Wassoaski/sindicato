package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.DocTypeNotFoundException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.repository.UserRepository;
import com.rafaelwassoaski.sindicato.service.secutiry.JWTService;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;
import com.rafaelwassoaski.sindicato.service.validation.docType.user.DocumentTypeExistenceValidation;
import com.rafaelwassoaski.sindicato.service.validation.user.UserIdExistenceValidation;
import com.rafaelwassoaski.sindicato.util.CookiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    private JWTService jwtService;


    public Document createDocument(DocumentDTO documentDTO) throws BaseException {
        CustomUser dtoCustomUser = documentDTO.getDocumentCustomUser();
        DocumentType documentType = this.getDocumentTypeByDTO(documentDTO, documentDTO.getDocumentTypeId());

        this.validateDocument(dtoCustomUser, documentType);

        String documentName = this.createDocumentName(documentDTO, documentType);
        documentDTO.setName(documentName);
        documentDTO.setCreatedAt(LocalDateTime.now());

        Document documentFromDTO = new Document(documentDTO, documentType);
        return documentRepository.save(documentFromDTO);
    }

    public Document updateDocument(Long documentId, DocumentDTO updatedDocumentDTO) throws ResourceNotFoundException, DocTypeNotFoundException {
        Document document = this.findDocumentById(documentId);
        DocumentType actualDocumentType = document.getDocumentType();

        updatedDocumentDTO.setDocumentUser(document.getDocumentCustomUser());
        updatedDocumentDTO.setCreatedAt(document.getCreatedAt());

        DocumentType documentType = this.getDocumentTypeByDTO(updatedDocumentDTO, actualDocumentType.getId());
        this.updateDocumentName(updatedDocumentDTO, actualDocumentType);
        document.updateDocument(updatedDocumentDTO, documentType);

        return this.documentRepository.saveAndFlush(document);
    }

    private DocumentDTO updateDocumentName(DocumentDTO updatedDocumentDTO, DocumentType actualDocumentType) throws DocTypeNotFoundException {
        Long documentDtoDocumentTypeId = updatedDocumentDTO.getDocumentTypeId();
        String newDocumentName = updatedDocumentDTO.getName();

        if(!actualDocumentType.getId().equals(documentDtoDocumentTypeId)
        && newDocumentName == null){
            DocumentType newDocumentType = this.getDocumentTypeByDTO(updatedDocumentDTO, actualDocumentType.getId());

            newDocumentName = this.createDocumentName(updatedDocumentDTO, newDocumentType);
        }

        updatedDocumentDTO.setName(newDocumentName);
        return updatedDocumentDTO;
    }

    private void validateDocument(CustomUser dtoCustomUser, DocumentType documentType) throws BaseException {
        ChainValidation userValidation = this.createUserValidation();
        ChainValidation docTypeValidation = this.createDocTypeValidation();

        userValidation.isValid(dtoCustomUser);
        docTypeValidation.isValid(documentType);
    }

    private DocumentType getDocumentTypeByDTO(DocumentDTO documentDTO, Long actualDocumentTypeId) throws DocTypeNotFoundException {
        Long documentTypeId = documentDTO.getDocumentTypeId();
        if(documentTypeId == null){
            documentTypeId = actualDocumentTypeId;
        }
        DocumentType documentType = documentTypeService.findById(documentTypeId);

        return documentType;
    }


    private String createDocumentName(DocumentDTO documentDTO, DocumentType documentType) {
        CustomUser user = documentDTO.getDocumentCustomUser();
        String documentName = user.getName() + "-" + documentType.getName();

        int userDocumentsTypeCount = this.documentRepository.countByName(documentName);
        userDocumentsTypeCount++;

        return documentName + "-" + userDocumentsTypeCount;
    }

    private ChainValidation createUserValidation() {
        UserIdExistenceValidation userExistenceValidation = new UserIdExistenceValidation(null, userRepository);

        return userExistenceValidation;
    }

    private ChainValidation createDocTypeValidation() {
        DocumentTypeExistenceValidation docTypeExistenceValidation = new DocumentTypeExistenceValidation(null, documentTypeRepository);

        return docTypeExistenceValidation;
    }

    public Document findDocumentById(java.lang.Long id) throws ResourceNotFoundException {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (!optionalDocument.isPresent()) {
            throw new ResourceNotFoundException(String.format("Não foi possível encontrar o documento de ID: %d", id));
        }

        return optionalDocument.get();
    }

    public List<Document> findDocumentUserId(java.lang.Long id) throws ResourceNotFoundException {
        CustomUser customUser = userService.findUserById(id);
        List<Document> userDocuments = documentRepository.findAllByDocumentCustomUser(customUser);
        return userDocuments;
    }

    public DocumentDTO createDocumentDTO(HttpServletRequest request) throws ResourceNotFoundException {
        CustomUser user = userService.findUserByCookie(request);
        return new DocumentDTO(user);
    }

    public List<Document> findAllDocuments() {
        return this.documentRepository.findAll();
    }
}
