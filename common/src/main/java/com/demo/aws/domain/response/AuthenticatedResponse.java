package com.demo.aws.domain.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthenticatedResponse implements Serializable {

    private String username;

    private String accessToken;

    private String idToken;

    private String refreshToken;

}
