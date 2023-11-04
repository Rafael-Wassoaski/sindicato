package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "syndicate_document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @ManyToOne
    private DocumentType documentType;
    @ManyToOne
    private CustomUser documentCustomUser;
    @Column
    private long documentValue;
    @Column
    private LocalDateTime createdAt;
    @Column
    private String obs;

    public Document(String name, DocumentType documentType, CustomUser customUser, long value, LocalDateTime createdAt, String obs) {
        this.name = name;
        this.documentType = documentType;
        this.documentCustomUser = customUser;
        this.documentValue = value;
        this.createdAt = createdAt;
        this.obs = obs;
    }

    public Document(Long id, String name, DocumentType documentType, CustomUser customUser, long value, LocalDateTime createdAt, String obs) {
        this.id = id;
        this.name = name;
        this.documentType = documentType;
        this.documentCustomUser = customUser;
        this.documentValue = value;
        this.createdAt = createdAt;
        this.obs = obs;
    }

    public Document(DocumentDTO documentDTO) {
        this.name = documentDTO.getName();
        this.documentType = documentDTO.getDocumentType();
        this.documentCustomUser = documentDTO.getDocumentUser();
        this.documentValue = documentDTO.getDocumentValue();
        this.createdAt = documentDTO.getCreatedAt();
        this.obs = documentDTO.getObs();
    }

    public Document() {}

    public String getName() {
        return name;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public CustomUser getDocumentUser() {
        return this.documentCustomUser;
    }

    public long getDocumentValue() {
        return this.documentValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getObs() {
        return obs;
    }

    public Long getId() {
        return id;
    }
}
