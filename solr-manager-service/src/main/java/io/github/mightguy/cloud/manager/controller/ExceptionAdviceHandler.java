package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.Response;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * This class {@code ExceptionAdviceHandler} is responsible for handling all the exceptions, 1.
 * System Exceptions inherinting {@code Exception} 2. Application Exceptions inheriting  {@code
 * SolrCommonsException}
 */
@ControllerAdvice
@RestController
@Slf4j
public class ExceptionAdviceHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final Response handleGenericException(Exception exception) {
    log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception);
    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
  }

  @ExceptionHandler(SolrCommonsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public final Response handleSolrException(SolrCommonsException ex) {
    log.error(ex.getExceptionCode().getHttpStatus().toString(), ex);
    return new Response(ex.getExceptionCode().getHttpStatus(), ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public final Response handleConstraintException(ConstraintViolationException ex,
      WebRequest request) {
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getMessage());
    }
    return new Response(HttpStatus.BAD_REQUEST, "Constraint Voilations", errors);
  }
}