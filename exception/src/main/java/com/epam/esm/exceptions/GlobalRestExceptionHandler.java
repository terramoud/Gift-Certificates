package com.epam.esm.exceptions;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Translator translator;

    @Autowired
    public GlobalRestExceptionHandler(Translator translator) {
        this.translator = translator;
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ApiErrorResponse> handleAllExceptions(Throwable ex) {
        ex.printStackTrace();
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorCode(ErrorCodes.INTERNAL_SERVER_ERROR);
        apiErrorResponse.setErrorMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<ApiErrorResponse> customerNotFound(SQLException ex) {
        ex.printStackTrace();
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorCode(String.valueOf(ex.getErrorCode()));
        apiErrorResponse.setErrorMessage(translator.toLocale("mysql.error." + ex.getErrorCode()));
        if (ex.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
            apiErrorResponse.setErrorCode(ErrorCodes.SQL_DUPLICATE_ENTRY);
        if (ex.getErrorCode() == MysqlErrorNumbers.ER_BAD_NULL_ERROR)
            apiErrorResponse.setErrorCode(ErrorCodes.SQL_NULL_ENTRY);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ex.printStackTrace();
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status))
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorMessage(translator.toLocale(ex.getMessage()));
        apiErrorResponse.setErrorCode(status.value() + ErrorCodes.SUFFIX_RESPONSE_ENTITY_EXCEPTIONS);
        if (ex instanceof NoHandlerFoundException && ex.getMessage().startsWith("No handler found for")) {
            apiErrorResponse.setErrorMessage(ex.getMessage().replace(
                    "No handler found for", translator.toLocale("no.handler.found.for")));
        }
        return new ResponseEntity<>(apiErrorResponse, headers, status);
    }

    @ExceptionHandler({InvalidResourcePropertyException.class})
    protected ResponseEntity<ApiErrorResponse> handleInvalidResourceProperty(InvalidResourcePropertyException ex) {
        return handleResourceException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidResourceNameException.class})
    protected ResponseEntity<ApiErrorResponse> handleInvalidResourceName(InvalidResourceNameException ex) {
        return handleResourceException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return handleResourceException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResourceNotUpdatedException.class})
    protected ResponseEntity<ApiErrorResponse> handleResourceNotUpdated(ResourceNotUpdatedException ex) {
        return handleResourceException(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ResourceNotSavedException.class})
    protected ResponseEntity<ApiErrorResponse> handleResourceNotSaved(ResourceNotSavedException ex) {
        return handleResourceException(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({IllegalStateException.class})
    protected ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        ex.printStackTrace();
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorCode(ErrorCodes.INTERNAL_SERVER_ILLEGAL_STATE);
        apiErrorResponse.setErrorMessage(ex.getLocalizedMessage());
        if (ex.getMessage().startsWith("get null list resources")) {
            apiErrorResponse.setErrorMessage(translator.toLocale(ex.getMessage().replace(
                    "get null list resources", "get.null.list.resources")));
            apiErrorResponse.setErrorCode(ErrorCodes.NULL_RESOURCE);
        }
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ApiErrorResponse> handleResourceException(GlobalResourceException ex, HttpStatus httpStatus) {
        ex.printStackTrace();
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrorCode(ex.getErrorCode());
        apiErrorResponse.setErrorMessage(translator.toLocale(ex.getMessage()) + " " + ex.getDetails());
        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }
}