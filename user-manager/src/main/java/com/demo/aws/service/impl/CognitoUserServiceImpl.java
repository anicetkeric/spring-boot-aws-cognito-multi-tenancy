package com.demo.aws.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.demo.aws.domain.enums.CognitoAttributesEnum;
import com.demo.aws.domain.model.CognitoCredentials;
import com.demo.aws.domain.request.UserSignUp;
import com.demo.aws.exceptions.FailedAuthenticationException;
import com.demo.aws.exceptions.ServiceException;
import com.demo.aws.service.CognitoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CognitoUserServiceImpl implements CognitoUserService {

    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    private static final String DEFAULT_PASSWORD = "p>K28fM9";

    /**
     * {@inheritDoc}
     */
    @Override
    public UserType signUp(UserSignUp userSignUp, CognitoCredentials credentials) {

        try {
            final AdminCreateUserRequest signUpRequest = new AdminCreateUserRequest()
                    .withUserPoolId(credentials.getPoolId())
                    // The user's temporary password.
                    .withTemporaryPassword(DEFAULT_PASSWORD)
                    // Specify "EMAIL" if email will be used to send the welcome message
                    .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                    .withUsername(userSignUp.getEmail())
                    .withMessageAction(MessageActionType.SUPPRESS)
                    .withUserAttributes(
                            new AttributeType().withName("name").withValue(userSignUp.getName()),
                            new AttributeType().withName("family_name").withValue(userSignUp.getLastname()),
                            new AttributeType().withName("custom:nationality").withValue(userSignUp.getNationality()),
                            new AttributeType().withName("email").withValue(userSignUp.getEmail()),
                            new AttributeType().withName("email_verified").withValue("true"),
                            new AttributeType().withName("phone_number").withValue(userSignUp.getPhoneNumber()),
                            new AttributeType().withName("phone_number_verified").withValue("true"));

            // create user
            AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(signUpRequest);
            log.info("Created User id: {}", createUserResult.getUser().getUsername());

            // assign the roles
            userSignUp.getRoles().forEach(r -> addUserToGroup(userSignUp.getEmail(), r, credentials.getPoolId()));

            // set permanent password
            setUserPassword(userSignUp.getEmail(), userSignUp.getPassword(), credentials.getPoolId());

            return createUserResult.getUser();

        } catch (com.amazonaws.services.cognitoidp.model.UsernameExistsException e) {
            throw new UsernameExistsException("User name that already exists");
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new InvalidPasswordException("Invalid password.");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSignOutResult signOut(String accessToken) {
        try {
            return awsCognitoIdentityProvider.globalSignOut(new GlobalSignOutRequest().withAccessToken(accessToken));
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(String.format("Logout failed: %s", e.getErrorMessage()), e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserToGroup(String username, String groupName, String poolId) {

        try {
            // add user to group
            AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                    .withGroupName(groupName)
                    .withUserPoolId(poolId)
                    .withUsername(username);

            awsCognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdminInitiateAuthResult> initiateAuth(String username, String password, CognitoCredentials credentials) {

        final Map<String, String> authParams = new HashMap<>();
        authParams.put(CognitoAttributesEnum.USERNAME.name(), username);
        authParams.put(CognitoAttributesEnum.PASSWORD.name(), password);
        authParams.put(CognitoAttributesEnum.SECRET_HASH.name(), calculateSecretHash(credentials.getClientId(), credentials.getClientSecret(), username));


        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withClientId(credentials.getClientId())
                .withUserPoolId(credentials.getClientSecret())
                .withAuthParameters(authParams);

        return adminInitiateAuthResult(authRequest);
    }



    private Optional<AdminInitiateAuthResult> adminInitiateAuthResult(AdminInitiateAuthRequest request) {
        try {
            return Optional.of(awsCognitoIdentityProvider.adminInitiateAuth(request));
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(String.format("Authenticate failed: %s", e.getErrorMessage()), e);
        } catch (UserNotFoundException e) {
            String username = request.getAuthParameters().get(CognitoAttributesEnum.USERNAME.name());
            throw new com.demo.aws.exceptions.UserNotFoundException(String.format("Username %s  not found.", username), e);
        }
    }

    private AdminSetUserPasswordResult setUserPassword(String username, String password, String poolId) {

        try {
            // Sets the specified user's password in a user pool as an administrator. Works on any user.
            AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
                    .withUsername(username)
                    .withPassword(password)
                    .withUserPoolId(poolId)
                    .withPermanent(true);

            return awsCognitoIdentityProvider.adminSetUserPassword(adminSetUserPasswordRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }
    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new ServiceException("Error while calculating ");
        }
    }
}
