package com.tien.iamservice_jwt.exception;

import com.tien.iamservice_jwt.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class) //bên kia throws new thi bên này nhận, exception inject
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
            ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setCode(ex.getErrorCode().getCode());
                    apiResponse.setMessage(ex.getErrorCode().getMessage());
            return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNKNOW_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.UNKNOW_ERROR.getMessage());
        return ResponseEntity.status(ErrorCode.UNKNOW_ERROR.getHttpStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessException() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.ACCESS_DENIED.getMessage());
        apiResponse.setCode(ErrorCode.ACCESS_DENIED.getCode());
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getHttpStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handlingMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
