package com.progressoft.training.payments.controller;


import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.fileparser.exception.DuplicateTemplateException;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = SystemViolationException.class)
    public ResponseEntity<String> handleException(SystemViolationException exception) {
        log.error("Validation failure", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler(value = DuplicateTemplateException.class)
    public ResponseEntity<String> handleDuplicateTemplate(DuplicateTemplateException exception) {
        log.error("Cant Add a template that already exists!", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(value = TemplateNotFoundException.class)
    public ResponseEntity<String> handleTemplateNotFound(TemplateNotFoundException exception) {
        log.error("Template Doesn't Exist!", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}