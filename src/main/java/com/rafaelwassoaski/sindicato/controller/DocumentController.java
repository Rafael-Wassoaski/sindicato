package com.rafaelwassoaski.sindicato.controller;

import com.rafaelwassoaski.sindicato.dto.DocumentDTO;
import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.entity.Document;
import com.rafaelwassoaski.sindicato.entity.DocumentType;
import com.rafaelwassoaski.sindicato.exceptions.BaseException;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.repository.DocumentRepository;
import com.rafaelwassoaski.sindicato.service.DocumentService;
import com.rafaelwassoaski.sindicato.service.DocumentTypeService;
import com.rafaelwassoaski.sindicato.service.UserService;
import com.rafaelwassoaski.sindicato.service.secutiry.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/myDocs/{id}")
    public String getMyDocuments(@PathVariable Long id, @RequestParam(name = "page") int page, Model model, HttpServletRequest request) throws BaseException {
        userService.sameUser(id, request);

        Page<Document> documents = documentService.findDocumentUserId(id, page);

        model.addAttribute("documents", documents.getContent());
        model.addAttribute("pages", documents.getTotalElements());

        return "documents/Documents";
    }

    @GetMapping("/allDocuments/")
    public String getAllDocuments(@RequestParam(name = "page") int page, Model model, HttpServletRequest request) throws BaseException {
        userService.userIsAdmin(request);
        Page<Document> documents = documentService.findAllDocuments(page);

        model.addAttribute("documents", documents.getContent());
        model.addAttribute("pages", documents.getTotalElements());

        return "documents/Documents";
    }

    @GetMapping("/create")
    public String crate(Model model, HttpServletRequest request) throws ResourceNotFoundException {
        DocumentDTO documentDTO = documentService.createDocumentDTO(request);

        //TODO: Remover esta criacao apos testes
        DocumentType documentType = new DocumentType("Tipo 1");
        documentTypeService.save(documentType);

        List<DocumentType> documentTypes = documentTypeService.getDocumentTypes();

        System.out.println("SDSFDSFSD " + documentDTO.getDocumentCustomUser());
        model.addAttribute("documentDTO", documentDTO);
        model.addAttribute("documentTypes", documentTypes);

        return "documents/Create";
    }

    @PostMapping("/newDoc")
    public String newDoc(Model model, DocumentDTO documentDTO, HttpServletRequest request) throws BaseException {
        try {
            CustomUser customUser = userService.findUserByCookie(request);
            documentDTO.setDocumentUser(customUser);
            this.documentService.createDocument(documentDTO);

            return this.getMyDocuments(customUser.getId(), 0, model, request);
        } catch (ResponseStatusException | BaseException e) {
            throw e;
        }
    }

    @GetMapping("/myDocs/{id}/{documentId}")
    public String getMyDocumentById(@PathVariable Long id, @PathVariable Long documentId, Model model, HttpServletRequest request) throws BaseException {
        userService.sameUser(id, request);

        Document document = documentService.findDocumentById(documentId);

        model.addAttribute("document", document);

        return "documents/Document";
    }

    @GetMapping("/myDocs/{id}/{documentId}/update")
    public String updateDocumentById(@PathVariable Long id, @PathVariable Long documentId, Model model, HttpServletRequest request) throws BaseException {
        userService.sameUser(id, request);

        Document document = documentService.findDocumentById(documentId);
        DocumentDTO documentDTO = new DocumentDTO(document);

        model.addAttribute("documentDTO", documentDTO);
        model.addAttribute("documentId", documentId);

        return "documents/Update";
    }

    @PostMapping("/myDocs/{id}/{documentId}/updateDoc")
    public String updateDocument(@PathVariable Long id, @PathVariable Long documentId, DocumentDTO documentDTO,  Model model, HttpServletRequest request) throws BaseException {
        userService.sameUserOrAdmin(id, request);

        Document document = documentService.updateDocument(documentId, documentDTO);

        model.addAttribute("document", document);

        return this.getMyDocumentById(id, documentId, model, request);
    }
}