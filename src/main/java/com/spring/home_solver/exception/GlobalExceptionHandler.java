package com.spring.home_solver.exception;

import com.spring.home_solver.DTO.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExc.class)
    public ResponseEntity<ApiErrorResponse> notFoundHandler(NotFoundExc exc){
        ApiErrorResponse res = new ApiErrorResponse(false, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(ApiErrorExc.class)
    public ResponseEntity<ApiErrorResponse> ApiErrorHandler(ApiErrorExc exc){
        ApiErrorResponse res = new ApiErrorResponse(false, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> argumentErrorHandler(MethodArgumentNotValidException exc) {
        Map<String, String> res = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach(
                objectError -> {
                    String field = objectError.getObjectName();
                    String message = objectError.getDefaultMessage();
                    res.put(field,message);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
