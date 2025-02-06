package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.DuplicatedException;
import ar.edu.utn.frbb.tup.persistence.exception.EstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorWithoutMateriasException;

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

    @ExceptionHandler(value = { MateriaNotFoundException.class })
    protected ResponseEntity<Object> handleMateriaNotFound(
            MateriaNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4041);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { AsignaturaNotFoundException.class })
    protected ResponseEntity<Object> handleAsignaturaNotFound(
            AsignaturaNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4042);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ProfesorNotFoundException.class })
    protected ResponseEntity<Object> handleProfesorNotFound(
            ProfesorNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4043);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { AlumnoNotFoundException.class })
    protected ResponseEntity<Object> handleAlumnoNotFound(
            AlumnoNotFoundException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4044);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ProfesorWithoutMateriasException.class })
    protected ResponseEntity<Object> handleProfesorWithoutMaterias(
            ProfesorWithoutMateriasException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4045);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { DuplicatedException.class })
    protected ResponseEntity<Object> handleDuplicated(
            DuplicatedException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4091);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(EstadoAsignaturaException.class)
    protected ResponseEntity<Object> handleEstadoAsignatura(
            EstadoAsignaturaException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4001);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4002);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { IllegalStateException.class })
    protected ResponseEntity<Object> handleIllegalState(
            IllegalStateException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4003);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { DatoInvalidoException.class })
    protected ResponseEntity<Object> handleDatoInvalido(
            DatoInvalidoException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4004);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { NotaException.class })
    protected ResponseEntity<Object> handleNota(
            NotaException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4005);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { CorrelatividadException.class })
    protected ResponseEntity<Object> handleCorrelatividad(
            CorrelatividadException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4006);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EstadoIncorrectoException.class)
    protected ResponseEntity<Object> handleEstadoIncorrecto(
            EstadoIncorrectoException ex, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(ex.getMessage());
        error.setErrorCode(4007);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }
        return new ResponseEntity(body, headers, status);
    }
}
