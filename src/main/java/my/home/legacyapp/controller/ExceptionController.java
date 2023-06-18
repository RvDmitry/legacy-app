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
        var businessError = new BusinessError();
        businessError.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(businessError, HttpStatus.NOT_FOUND);
    }
}
