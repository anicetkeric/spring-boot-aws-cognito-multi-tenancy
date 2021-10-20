package com.demo.aws.domain.model.response;

import lombok.*;

/**
 * <h2>SuccessResponse</h2>
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
    private Object data;
    /**
     * error code
     */
    private String code;
    /**
     * short error message
     */
    private String message;

}
