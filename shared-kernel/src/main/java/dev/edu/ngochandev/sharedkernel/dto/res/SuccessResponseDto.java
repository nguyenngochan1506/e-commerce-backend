package dev.edu.ngochandev.sharedkernel.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseDto<T> {
    private final String status;
    private final String message;
    private final T data;

    @Builder
    public SuccessResponseDto(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus.name().toLowerCase();
        this.message = message;
        this.data = data;
    }
}
