package com.phonix.repositery;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phonix.model.Document;

public interface DocumentRepositery extends JpaRepository<Document, Integer> {
    Page<Document> findAll(Pageable pageable);
}
