package dev.edu.ngochandev.sharedkernel.exception;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Translator translator;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGlobalException(Exception ex, WebRequest req) {
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, message, req);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleResourceNotFound(Exception ex, WebRequest req) {
        return new ErrorResponseDto(HttpStatus.NOT_FOUND, translator.translate(ex.getMessage()), req);
    }

    @ExceptionHandler(DateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleDateFormatException(DateFormatException ex, WebRequest req) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, translator.translate(ex.getMessage()), req);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        ErrorResponseDto res =
                new ErrorResponseDto(HttpStatus.BAD_REQUEST, translator.translate("error.body.invalid"), req);
        if (ex.getBindingResult().hasErrors()) {
            ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
                String message = translator.translate(fieldError.getDefaultMessage());
                res.addValidationError(fieldError.getField(), message);
            });
        }
        return res;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest req) {
        String message = ex.getMessage();
        int startIndex = message.indexOf("[") + 1;
        int endIndex = message.indexOf("]");
        message = message.substring(startIndex, endIndex);
        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST, translator.translate("error.enum.invalid") + " " + message, req);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleDuplicateResourceException(DuplicateResourceException ex, WebRequest req) {
        return new ErrorResponseDto(HttpStatus.CONFLICT, translator.translate(ex.getMessage()), req);
    }
}
