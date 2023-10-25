package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);
}
