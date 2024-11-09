package com.architrave.portfolio.projectElement.textBox;

import com.architrave.portfolio.api.service.TextBoxService;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.repository.TextBoxRepository;
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
public class TextBoxUnitTest {

    @Mock
    private TextBoxRepository textBoxRepository;
    @InjectMocks
    private TextBoxService textBoxService;
    private TextBox testTextBox;

    @BeforeEach
    void setUp() {
        testTextBox = TextBox.createTextBox("Initial Content");
    }

    @Test
    @DisplayName("findById should return textBox")
    void findById_ShouldReturnTextBox() {
        // Arrange
        Long id = 1L;
        when(textBoxRepository.findById(id))
                .thenReturn(Optional.of(testTextBox));

        // Act
        TextBox result = textBoxService.findById(id);

        // Assert
        assertThat(result).isEqualTo(testTextBox);
        verify(textBoxRepository).findById(id);
    }

    @Test
    @DisplayName("findById should throw exception when not found")
    void findById_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(textBoxRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> textBoxService.findById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no textBox id:" + id);
    }

    @Test
    @DisplayName("createTextBox should save and return textBox")
    void createTextBox_ShouldSaveAndReturnTextBox() {
        // Arrange
        when(textBoxRepository.save(any(TextBox.class)))
                .thenReturn(testTextBox);

        // Act
        TextBox result = textBoxService.createTextBox("Test Content");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Initial Content");
        verify(textBoxRepository).save(any(TextBox.class));
    }

    @Test
    @DisplayName("updateTextBox should modify and return updated textBox")
    void updateTextBox_ShouldModifyAndReturnUpdatedTextBox() {
        // Arrange
        Long id = 1L;
        when(textBoxRepository.findById(id)).thenReturn(Optional.of(testTextBox));

        // Act
        TextBox result = textBoxService.updateTextBox(id, "Updated Content");

        // Assert
        assertThat(result.getContent()).isEqualTo("Updated Content");
        verify(textBoxRepository).findById(id);
    }

    @Test
    @DisplayName("removeTextBox should delete textBox")
    void removeTextBox_ShouldDeleteTextBox() {
        // Arrange
        Long id = 1L;
        when(textBoxRepository.findById(id)).thenReturn(Optional.of(testTextBox));

        // Act
        textBoxService.removeTextBox(id);

        // Assert
        verify(textBoxRepository).delete(testTextBox);
    }
}
