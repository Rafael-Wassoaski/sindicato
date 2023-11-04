package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    public Optional<CustomUser> findByEmail(String email);
}
