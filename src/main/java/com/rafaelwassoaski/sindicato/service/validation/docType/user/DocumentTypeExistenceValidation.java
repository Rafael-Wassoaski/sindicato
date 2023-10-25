package com.rafaelwassoaski.sindicato.service.validation.docType.user;

import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.DocTypeNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import com.rafaelwassoaski.sindicato.service.validation.ChainValidation;

import java.util.Optional;

public class DocumentTypeExistenceValidation extends ChainValidation<DocumentType> {

    private DocumentTypeRepository documentTypeRepository;

    public DocumentTypeExistenceValidation(ChainValidation nextValidation, DocumentTypeRepository documentTypeRepository) {
        super(nextValidation);
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public boolean isValid(DocumentType objectToValidate) throws BaseException {
        Optional<DocumentType> optionalDatabaseDocType = documentTypeRepository.findById(objectToValidate.getId());

        if (!optionalDatabaseDocType.isPresent()) {
            throw new DocTypeNotFoundException(objectToValidate.getName());
        }

        return this.callNext(objectToValidate);
    }
}
