package com.rafaelwassoaski.sindicato.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class DocumentType{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "documentType",cascade = CascadeType.ALL)
    private Set<Document> documents;

    public DocumentType(String name) {
        this.name = name;
    }

    public DocumentType(Long id, String name, Set<Document> documents) {
        this.id = id;
        this.name = name;
        this.documents = documents;
    }

    public DocumentType() {
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Set<Document> getDocuments() {
        return documents;
    }
}
