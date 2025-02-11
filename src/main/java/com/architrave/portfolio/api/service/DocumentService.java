package com.architrave.portfolio.api.service;


import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.repository.DocumentRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Trace
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UploadFileService uploadFileService;

    @Transactional(readOnly = true)
    public Document findDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no document that id:" + id));
    }
    @Transactional
    public Document createDocument(
            String originUrl,
            String description
    ){
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .build();

        Document document = Document.createDocument(
                uploadFile,
                description
        );
        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocument(
            Long documentId,
            String description,
            String originUrl
    ){
        Document document = findDocumentById(documentId);
        if(!document.getDescription().equals(description)) document.setDescription(description);
        if(!document.getUploadFile().getOriginUrl().equals(originUrl)
        ){
            uploadFileService.deleteUploadFile(document.getUploadFile());
            document.setUploadFileUrl(originUrl);
        }
        return document;
    }

    @Transactional
    public void removeDocument(Long documentId) {
        Document document = findDocumentById(documentId);
        uploadFileService.deleteUploadFile(document.getUploadFile());
        documentRepository.delete(document);
    }
}
