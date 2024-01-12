package com.rafaelwassoaski.sindicato.repository;

import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    public Page<Document> findAllByDocumentCustomUser(CustomUser documentCustomUser, Pageable pageable);

    public int countByName(String name);

    public List<Document> findAllByNameIsContaining(String name);
    public List<Document> findAllByDocumentValue(long value);
    @Query("SELECT d FROM Document d WHERE TO_CHAR(d.createdAt, 'YYYY-MM-DD HH:MI:SS') LIKE %?1%")
    public List<Document> findAllDocumentsCreatedInDate(String date);
    public List<Document> findByObsContains(String obs);

}
