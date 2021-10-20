package com.demo.aws.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CognitoCredentials {

    private String poolId;

    private String clientId;

    private String clientSecret;
}
