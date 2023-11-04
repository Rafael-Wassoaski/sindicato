package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    public List<Document> findAllByDocumentCustomUser(CustomUser documentCustomUser);
}
