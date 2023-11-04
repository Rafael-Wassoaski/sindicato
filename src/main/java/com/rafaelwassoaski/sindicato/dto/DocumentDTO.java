package com.rafaelwassoaski.sindicato.dto;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.DocumentType;

import java.time.LocalDateTime;

public class DocumentDTO {
    private String name;
    private DocumentType documentType;
    private CustomUser documentCustomUser;
    private long documentValue;
    private LocalDateTime createdAt;
    private String obs;

    public DocumentDTO(String name, DocumentType documentType, CustomUser documentCustomUser, long documentValue, LocalDateTime createdAt, String obs) {
        this.name = name;
        this.documentType = documentType;
        this.documentCustomUser = documentCustomUser;
        this.documentValue = documentValue;
        this.createdAt = createdAt;
        this.obs = obs;
    }

    public DocumentDTO(String name, DocumentType documentType, CustomUser documentCustomUser, long documentValue, LocalDateTime createdAt) {
        this.name = name;
        this.documentType = documentType;
        this.documentCustomUser = documentCustomUser;
        this.documentValue = documentValue;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public CustomUser getDocumentUser() {
        return documentCustomUser;
    }

    public void setDocumentUser(CustomUser documentCustomUser) {
        this.documentCustomUser = documentCustomUser;
    }

    public long getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(long documentValue) {
        this.documentValue = documentValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
