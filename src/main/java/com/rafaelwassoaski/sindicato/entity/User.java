package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.dto.UserDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "custom_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String email;
    @Embedded
    private Address address;
    @Column
    private String cpf;
    @OneToMany(mappedBy = "documentUser", cascade = CascadeType.ALL)
    private Set<Document> documents;
    public User(String name, String password, String email, Address address, String cpf) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
    }

    public User(Long id, String name, String password, String email, Address address, String cpf, Set<Document> documents) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
        this.documents = documents;
    }

    public User(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.password = userDTO.getPassword();
        this.email = userDTO.getEmail();
        this.address = userDTO.getAddress();
        this.cpf = userDTO.getCpf();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getCpf() {
        return cpf;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void addDocument(Document document){
        if(this.documents == null){
            this.documents = new HashSet<>();
        }

        this.documents.add(document);
    }
}
