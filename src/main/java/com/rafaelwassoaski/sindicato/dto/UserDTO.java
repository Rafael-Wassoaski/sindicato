package com.rafaelwassoaski.sindicato.dto;

import com.rafaelwassoaski.sindicato.entity.Address;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.entity.User;

import javax.persistence.Column;
import javax.persistence.Embedded;
import java.time.LocalDateTime;

public class UserDTO {
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

    public UserDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public UserDTO(String name, String password, String email, Address address, String cpf) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cpf = cpf;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
