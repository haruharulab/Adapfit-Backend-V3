package com.harulab.adapfit.global.error;

import com.harulab.adapfit.global.error.exception.AdapfitException;
import com.harulab.adapfit.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AdapfitException.class)
    public ResponseEntity<ErrorResponse> handleGlobal(AdapfitException e) {
        final ErrorCode errorCode = e.getErrorCode();
        log.error("code : " + errorCode.getCode());
        log.error("status : " + errorCode.getStatus());
        log.error("message : " + errorCode.getMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(errorCode.getStatus())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build(),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> bindException(BindException e) {
        Map<String, String> errorMap = new HashMap<>();

        for (FieldError error : e.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
            log.error(error.toString());
            log.error(error.getDefaultMessage());
        }
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();

        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            errorMap.put("constraint error", error.getMessage());
            log.error(error.toString());
        }

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}