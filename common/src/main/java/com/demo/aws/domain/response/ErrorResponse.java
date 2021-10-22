package com.demo.aws.domain.response;

import lombok.*;

/**
 * <h2>ErrorResponse</h2>
 *
 * @author aek
 * <p>
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ErrorResponse {

    /**
     * error code
     */
    private String code;
    /**
     * short error message
     */
    private String message;

    /**
     * More details about the error here
     */
    private String description;


    private Object errors;
}
