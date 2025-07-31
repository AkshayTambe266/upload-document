package com.phonix.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phonix.model.Document;
import com.phonix.repositery.DocumentRepositery;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepositery repository;

    @Value("${upload.dir}")
    private String uploadDir;

    public DocumentService(DocumentRepositery repository) {
        this.repository = repository;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        // Use a fixed uploads directory in the current project folder
        String uploadDir = System.getProperty("user.dir") + "/upload";
        File dir = new File(uploadDir);

        // Create directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Save the uploaded file
        String filePath = uploadDir + "/" + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        // Save document info to database
        Document doc = new Document();
        doc.setName(file.getOriginalFilename());
        doc.setPath(filePath);
        repository.save(doc);
    }

    public Page<Document> getDocuments(int page) {
        return repository.findAll(PageRequest.of(page, 5));
    }

    public Optional<Document> getDocument(Integer id) {
        return repository.findById(id);
    }

    public void deleteDocument(Integer id) {
        repository.findById(id).ifPresent(doc -> {
            new File(doc.getPath()).delete();
            repository.deleteById(id);
        });
    }
}
