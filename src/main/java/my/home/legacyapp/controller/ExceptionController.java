package my.home.legacyapp.controller;

import my.home.legacyapp.dto.BusinessError;
import my.home.legacyapp.exception.BusinessNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<BusinessError> handleException(BusinessNotFoundException ex) {
        return new ResponseEntity<>(new BusinessError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
