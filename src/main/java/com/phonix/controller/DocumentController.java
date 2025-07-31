package com.phonix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.phonix.model.Document;
import com.phonix.services.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.IOException;

@Controller
public class DocumentController {
@Autowired
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String listDocuments(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Document> docs = service.getDocuments(page);
        model.addAttribute("docs", docs);
        model.addAttribute("currentPage", page);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        service.uploadFile(file);
        return "redirect:/upload";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public byte[] downloadFile(@PathVariable Integer id) throws IOException {
        return service.getDocument(id)
                .map(doc -> {
                    try {
                        return java.nio.file.Files.readAllBytes(new java.io.File(doc.getPath()).toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow();
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Integer id) {
        service.deleteDocument(id);
        return "redirect:/delete";
    }
}
