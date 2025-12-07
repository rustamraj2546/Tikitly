package com.rkumar.tikitly.advices;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private T data;
    private ApiErrorResponse error;


    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiErrorResponse error) {
        this();
        this.error = error;
    }
}
