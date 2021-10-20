package com.demo.aws.service;

import com.amazonaws.services.cognitoidp.model.UserType;
import com.demo.aws.domain.model.Login;
import com.demo.aws.domain.model.UserSignUp;
import com.demo.aws.domain.model.response.SuccessResponse;

import javax.validation.constraints.NotNull;

public interface UserService {

    /**
     * @param userLogin user login infos
     * @return BaseResponse
     */
    SuccessResponse authenticate(Login userLogin);


    /**
     * @param accessToken user authenticate access token
     */
    void logout(@NotNull String accessToken);

    /**
     * Creates a new user in the specified user pool.
     *
     * @param signUp user info
     * @return UserType
     */
    UserType createUser(UserSignUp signUp);

}
