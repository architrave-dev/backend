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
            String thumbnailUrl,
            String description
    ){
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
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
            String originUrl,
            String thumbnailUrl
    ){
        Document document = findDocumentById(documentId);
        if(!document.getDescription().equals(description)) document.setDescription(description);
        if(!document.getUploadFile().getOriginUrl().equals(originUrl) ||
                !document.getUploadFile().getThumbnailUrl().equals(thumbnailUrl)
        ){
            uploadFileService.deleteUploadFile(document.getUploadFile());
            document.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        return document;
    }

    @Transactional
    public void removeDocument(Long documentId) {
        Document document = findDocumentById(documentId);
        documentRepository.delete(document);
    }
}
