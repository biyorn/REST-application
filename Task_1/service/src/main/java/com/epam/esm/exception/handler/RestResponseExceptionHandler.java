package com.epam.esm.exception.handler;

import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.FailedUpdateObjectException;
import com.epam.esm.exception.IncorrectParameterException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.exception.PageNotFoundException;
import com.epam.esm.exception.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        logger.info(message, ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("Incorrect parameters"));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        logger.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("Incorrect parameter - [" + exception.getValue() + "]"));
    }

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<?> handleUserException(UserException exception) {
        logger.error(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FailedAddObjectException.class)
    public ResponseEntity<?> handleFailedAddObject(FailedAddObjectException exception) {
        logger.error(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = FailedUpdateObjectException.class)
    public ResponseEntity<?> handleFailedUpdateObject(FailedUpdateObjectException exception) {
        logger.error(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(value = IncorrectParameterException.class)
    public ResponseEntity<?> handleIncorrectParameter(IncorrectParameterException exception) {
        logger.error(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundObjectException.class)
    public ResponseEntity<?> handleThereIsNotFoundObject(NotFoundObjectException exception) {
        logger.error(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PageNotFoundException.class)
    public ResponseEntity<?> handlePageNotFound(PageNotFoundException exception) {
        logger.info(exception.getMessage(), exception);
        return createResponse(exception, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> createResponse(Exception exception, HttpStatus status) {
        String message = exception.getMessage();
        ApiError apiError = new ApiError(message);

        return ResponseEntity
                .status(status)
                .body(apiError);
    }
}
