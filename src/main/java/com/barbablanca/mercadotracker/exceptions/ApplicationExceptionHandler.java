package com.barbablanca.mercadotracker.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException exception, WebRequest request) {

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(401, exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ IdNotFoundException.class })
    public ResponseEntity<ExceptionResponse> handleIdNotFound(IdNotFoundException exception, WebRequest request) {

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ProductNotFoundException.class })
    public ResponseEntity<ExceptionResponse> handleProductNotFound(ProductNotFoundException exception, WebRequest request) {

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(404, exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ SinginException.class })
    public ResponseEntity<ExceptionResponse> handleSinginException(SinginException exception, WebRequest request) {

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(DataIntegrityViolationException exception, WebRequest request) {

        String message = "Hay un error con algun dato";

        if (exception.getMostSpecificCause().getMessage().contains("user_email_uq"))
            message = "El correo electronico ya existe";

        if (exception.getMostSpecificCause().getMessage().contains("user_name_uq"))
            message = "El nombre de usuario ya existe";

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(400, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ PatchException.class })
    public  ResponseEntity<ExceptionResponse> handlePatchException(PatchException exception, WebRequest request) {
        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BadPasswordResetAttempt.class })
    public  ResponseEntity<ExceptionResponse> handleBadPasswordResetAttempt(BadPasswordResetAttempt exception, WebRequest request) {
        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ExceptionResponse> handleAccesDeniedException(AccessDeniedException exception, WebRequest request) {

        return new ResponseEntity<ExceptionResponse>(
                new ExceptionResponse(401, "No se pudo corroborar su identidad, inicie sesion"), HttpStatus.UNAUTHORIZED);
    }
}
