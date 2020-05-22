package com.barbablanca.mercadotracker.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler {

    Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler({ CustomException.class })
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException exception) {

        log.error(exception.getMessage());

        return new ResponseEntity<>(
                new ExceptionResponse(exception.getCode(), exception.getMessage()),
                HttpStatus.valueOf(exception.getCode())
        );
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ExceptionResponse> handleBadParameters(
            MethodArgumentNotValidException exception,
            WebRequest request) {

        log.error(exception.getMessage());

        String message = exception.getBindingResult().hasFieldErrors() ?
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage() :
                exception.getMessage();

        return new ResponseEntity<>(
                new ExceptionResponse(400, message
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(DataIntegrityViolationException exception, WebRequest request) {

        log.error(exception.getMessage());

        String message = "Hay un error con algun dato";

        if (exception.getMostSpecificCause().getMessage().contains("user_email_uq"))
            message = "El correo electronico ya existe";

        if (exception.getMostSpecificCause().getMessage().contains("user_name_uq"))
            message = "El nombre de usuario ya existe";

        return new ResponseEntity<>(
                new ExceptionResponse(400, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ExceptionResponse> handleAccesDeniedException(AccessDeniedException exception, WebRequest request) {

        log.error(exception.getMessage());

        return new ResponseEntity<>(
                new ExceptionResponse(401, "No se pudo corroborar su identidad, inicie sesion"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ExceptionResponse> handleUnknownException(Exception exception, WebRequest request) {

        log.error(exception.getMessage());

        return new ResponseEntity<>(
                new ExceptionResponse(400, "Ocurrio un error"), HttpStatus.BAD_REQUEST);
    }
}
