package org.freightfox.freightfox_weatherapis.Response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String status;           // Success or error status
    private String message;          // Message to describe the response outcome
    private Object data;             // Data returned from the service
    private String error;            // Error message in case of failure (can be null)
    private LocalDateTime timestamp; // Time the response was generated
}
