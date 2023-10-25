package com.rafaelwassoaski.sindicato.dto;

import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.User;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class DocumentDTO {
    private String name;
    private DocumentType documentType;
    private User documentUser;
    private long documentValue;
    private LocalDateTime createdAt;
    private String obs;

    public DocumentDTO(String name, DocumentType documentType, User documentUser, long documentValue, LocalDateTime createdAt, String obs) {
        this.name = name;
        this.documentType = documentType;
        this.documentUser = documentUser;
        this.documentValue = documentValue;
        this.createdAt = createdAt;
        this.obs = obs;
    }

    public DocumentDTO(String name, DocumentType documentType, User documentUser, long documentValue, LocalDateTime createdAt) {
        this.name = name;
        this.documentType = documentType;
        this.documentUser = documentUser;
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

    public User getDocumentUser() {
        return documentUser;
    }

    public void setDocumentUser(User documentUser) {
        this.documentUser = documentUser;
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
