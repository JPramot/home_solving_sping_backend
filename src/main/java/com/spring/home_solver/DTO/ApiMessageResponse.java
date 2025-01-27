package com.spring.home_solver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageResponse {
    private Message message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String message;
    }
}
