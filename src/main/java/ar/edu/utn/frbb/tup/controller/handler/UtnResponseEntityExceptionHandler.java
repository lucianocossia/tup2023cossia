package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UtnResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {MateriaNotFoundException.class})
    protected ResponseEntity<Object> handleMateriaNotFound(
            MateriaNotFoundException ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1234);
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }



    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }

        return new ResponseEntity(body, headers, status);
    }


}
