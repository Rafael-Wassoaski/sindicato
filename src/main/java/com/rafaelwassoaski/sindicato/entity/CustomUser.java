package com.rafaelwassoaski.sindicato.entity;

import com.rafaelwassoaski.sindicato.dto.UserDTO;
import com.rafaelwassoaski.sindicato.enums.Roles;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
    @Enumerated(EnumType.STRING)
    @Column
    private Roles role;
    @OneToMany(mappedBy = "documentCustomUser", cascade = CascadeType.ALL)
    private Set<Document> documents;
    public CustomUser(String name, String password, String email, Address address, String cpf, Roles role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
        this.role = role;
    }

    public CustomUser(Long id, String name, String password, String email, Address address, String cpf, Set<Document> documents, Roles role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
        this.documents = documents;
        this.role = role;
    }

    public CustomUser(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.password = userDTO.getPassword();
        this.email = userDTO.getEmail();
        this.address = userDTO.getAddress();
        this.cpf = userDTO.getCpf();
        this.role = Roles.USER;
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
    public Roles getRole() {
        return this.role;
    }

    public String[] getUserRoleAsArray(){
        return new String[]{this.role.name()};
    }

    public void addDocument(Document document){
        if(this.documents == null){
            this.documents = new HashSet<>();
        }

        this.documents.add(document);
    }

    public boolean isAdmin() {
        return this.role.equals(Roles.ADMIN);
    }
}
