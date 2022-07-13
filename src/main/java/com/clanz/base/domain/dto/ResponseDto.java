package com.clanz.base.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private String status;
    private String message;
    private T result;

    public static <T> ResponseDto<T> createFailMessage(String message) {
        return new ResponseDto<>("fail", message, null);
    }

    public static <T> ResponseDto<T> createSuccessMessage() {
        return createSuccessMessage("The operation was successful");
    }

    public static <T> ResponseDto<T> createSuccessMessage(String message) {
        return createSuccessMessage(message, null);
    }

    public static <T> ResponseDto<T> createSuccessMessage(T result) {
        return new ResponseDto<>("success", "The operation was successful", result);
    }

    public static <T> ResponseDto<T> createSuccessMessage(String message, T result) {
        return new ResponseDto<>("success", message, result);
    }
}
