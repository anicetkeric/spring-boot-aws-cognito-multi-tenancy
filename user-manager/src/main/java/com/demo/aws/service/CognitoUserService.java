package com.demo.aws.service;


import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.demo.aws.domain.model.CognitoCredentials;
import com.demo.aws.domain.model.UserSignUp;

import java.util.Optional;

public interface CognitoUserService {


    /**
     * Authenticate Cognito User
     *
     * @param username user name
     * @param password user password
     * @return Optional<AdminInitiateAuthResult>
     */
    Optional<AdminInitiateAuthResult> initiateAuth(String username, String password, CognitoCredentials credentials);

    /**
     * Add a group to user
     *
     * @param username  user name
     * @param groupName group name
     */
    void addUserToGroup(String username, String groupName, String poolId);


    /**
     * Creates a new user in the specified user pool.
     *
     * @param signUp user info
     * @param credentials aws cognito credentials
     * @return UserType
     */
    UserType signUp(UserSignUp signUp, CognitoCredentials credentials);


    /**
     * Signs out users from all devices.
     *
     * @param accessToken access token
     * @return GlobalSignOutResult
     */
    GlobalSignOutResult signOut(String accessToken);
}
