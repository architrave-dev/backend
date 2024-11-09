package com.architrave.portfolio.projectElement.document;

import com.architrave.portfolio.api.service.DocumentService;
import com.architrave.portfolio.domain.model.Document;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentUnitTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document testDocument;

    @BeforeEach
    void setUp() {
        UploadFile testUploadFile = UploadFile.builder()
                .originUrl("document-origin.jpg")
                .thumbnailUrl("document-thumbnail.jpg")
                .build();

        testDocument = Document.createDocument(testUploadFile, "Test Description");
    }

    @Test
    @DisplayName("findDocumentById should return document")
    void findDocumentById_ShouldReturnDocument() {
        // Arrange
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.of(testDocument));

        // Act
        Document result = documentService.findDocumentById(id);

        // Assert
        assertThat(result).isEqualTo(testDocument);
        verify(documentRepository).findById(id);
    }

    @Test
    @DisplayName("findDocumentById should throw exception when not found")
    void findDocumentById_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> documentService.findDocumentById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no document that id:" + id);
    }

    @Test
    @DisplayName("createDocument should save and return document")
    void createDocument_ShouldSaveAndReturnDocument() {
        // Arrange
        when(documentRepository.save(any(Document.class))).thenReturn(testDocument);

        // Act
        Document result = documentService.createDocument(
                "document-origin.jpg",
                "document-thumbnail.jpg",
                "Test Description"
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getUploadFile().getOriginUrl()).isEqualTo("document-origin.jpg");
        assertThat(result.getUploadFile().getThumbnailUrl()).isEqualTo("document-thumbnail.jpg");
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    @DisplayName("updateDocument should modify and return updated document")
    void updateDocument_ShouldModifyAndReturnUpdatedDocument() {
        // Arrange
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.of(testDocument));

        // Act
        Document result = documentService.updateDocument(
                id,
                "Updated Description",
                "updated-origin.jpg",
                "updated-thumbnail.jpg"
        );

        // Assert
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getUploadFile().getOriginUrl()).isEqualTo("updated-origin.jpg");
        assertThat(result.getUploadFile().getThumbnailUrl()).isEqualTo("updated-thumbnail.jpg");
        verify(documentRepository).findById(id);
    }

    @Test
    @DisplayName("removeDocument should delete document")
    void removeDocument_ShouldDeleteDocument() {
        // Arrange
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.of(testDocument));

        // Act
        documentService.removeDocument(id);

        // Assert
        verify(documentRepository).delete(testDocument);
    }
}
