package com.progressoft.training.payments.controlleradvice;


import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.fileparser.exception.DuplicateTemplateException;
import com.progressoft.training.fileparser.exception.FileParserException;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
    @ExceptionHandler(value= FileParserException.class)
    public ResponseEntity<String> handleAllFIleParserExceptions( FileParserException exception){
        log.error("File Parser Exception!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}