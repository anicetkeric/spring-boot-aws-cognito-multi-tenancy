package com.demo.aws.service.impl;

import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.demo.aws.TenantContext;
import com.demo.aws.domain.TenantMaster;
import com.demo.aws.domain.model.*;
import com.demo.aws.domain.request.AuthenticatedChallenge;
import com.demo.aws.domain.request.Login;
import com.demo.aws.domain.request.UserSignUp;
import com.demo.aws.domain.response.AuthenticatedResponse;
import com.demo.aws.domain.response.SuccessResponse;
import com.demo.aws.exceptions.TenantNotFoundException;
import com.demo.aws.exceptions.UserNotFoundException;
import com.demo.aws.service.CognitoUserService;
import com.demo.aws.service.TenantMasterService;
import com.demo.aws.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.amazonaws.services.cognitoidp.model.ChallengeNameType.NEW_PASSWORD_REQUIRED;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final TenantMasterService tenantMasterService;

    private final CognitoUserService cognitoUserService;


    /**
     * @return CognitoCredentials
     */
    private CognitoCredentials extractCognitoCredentials(){
        Optional<TenantMaster> tenantMaster = tenantMasterService.getTenantById(TenantContext.getTenantId());
        if (tenantMaster.isPresent() && tenantMaster.get().isStatus()){
            return CognitoCredentials.builder()
                    .clientId(tenantMaster.get().getAppClientId())
                    .clientSecret(tenantMaster.get().getAppSecretId())
                    .poolId(tenantMaster.get().getUserPoolId())
                    .build();
        }else {
            throw new TenantNotFoundException("Tenant Data not found or not activated");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuccessResponse authenticate(Login userLogin) {
        CognitoCredentials credentials = extractCognitoCredentials();
        AdminInitiateAuthResult result = cognitoUserService.initiateAuth(userLogin.getUsername(), userLogin.getPassword(), credentials)
                .orElseThrow(() -> new UserNotFoundException(String.format("Username %s  not found.", userLogin.getUsername())));

        // Password change required on first login
        if (ObjectUtils.nullSafeEquals(NEW_PASSWORD_REQUIRED.name(), result.getChallengeName())) {
            return new SuccessResponse(AuthenticatedChallenge.builder()
                    .challengeType(NEW_PASSWORD_REQUIRED.name())
                    .sessionId(result.getSession())
                    .username(userLogin.getUsername())
                    .build(), "First time login - Password change required");
        }

        return new SuccessResponse(AuthenticatedResponse.builder()
                .accessToken(result.getAuthenticationResult().getAccessToken())
                .idToken(result.getAuthenticationResult().getIdToken())
                .refreshToken(result.getAuthenticationResult().getRefreshToken())
                .username(userLogin.getUsername())
                .build(), "Login successful");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(@NotNull String accessToken) {
        cognitoUserService.signOut(accessToken);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UserType createUser(UserSignUp signUp) {
        CognitoCredentials credentials = extractCognitoCredentials();

        return cognitoUserService.signUp(signUp, credentials);
    }
}
