package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.dto.UserDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "custom_user")
public class CustomUser {
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
    @OneToMany(mappedBy = "documentCustomUser", cascade = CascadeType.ALL)
    private Set<Document> documents;
    public CustomUser(String name, String password, String email, Address address, String cpf) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
    }

    public CustomUser(Long id, String name, String password, String email, Address address, String cpf, Set<Document> documents) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
        this.documents = documents;
    }

    public CustomUser(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.password = userDTO.getPassword();
        this.email = userDTO.getEmail();
        this.address = userDTO.getAddress();
        this.cpf = userDTO.getCpf();
    }

    public CustomUser() {
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
