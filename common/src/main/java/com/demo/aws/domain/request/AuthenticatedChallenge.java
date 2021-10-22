package com.demo.aws.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedChallenge {

    @NotBlank
    private String sessionId;

    @NonNull
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank
    private String challengeType;
}
