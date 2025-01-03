package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.repository.TextBoxRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class TextBoxService {

    private final TextBoxRepository textBoxRepository;

    @Transactional(readOnly = true)
    public TextBox findById(Long id) {
        return textBoxRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no textBox id:" + id));
    }

    @Transactional
    public TextBox createTextBox(TextBox textBox) {
        return textBoxRepository.save(textBox);
    }
    @Transactional
    public TextBox createTextBox(String content) {
        TextBox textBox = TextBox.createTextBox(content);
        return textBoxRepository.save(textBox);
    }

    @Transactional
    public TextBox updateTextBox(Long textBoxId, String content) {
        TextBox textBox = findById(textBoxId);
        if(content != null) textBox.setContent(content);

        return textBox;
    }

    public void removeTextBox(Long textBoxId) {
        TextBox textBox = findById(textBoxId);
        textBoxRepository.delete(textBox);
    }
}
