package com.rafaelwassoaski.sindicato.service;

import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.exceptions.DocTypeNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    public List<DocumentType> getDocumentTypes(){
        return this.documentTypeRepository.findAll();
    }

    public DocumentType save(DocumentType documentType){
        return this.documentTypeRepository.save(documentType);
    }

    public DocumentType findById(Long id) throws DocTypeNotFoundException {
        Optional<DocumentType> optionalDocumentType = documentTypeRepository.findById(id);

        if(!optionalDocumentType.isPresent()){
            throw new DocTypeNotFoundException("De id: " + id);
        }

        return optionalDocumentType.get();
    }
}
