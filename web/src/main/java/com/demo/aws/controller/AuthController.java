package com.demo.aws.controller;

import com.amazonaws.services.cognitoidp.model.UserType;
import com.demo.aws.domain.model.Login;
import com.demo.aws.domain.model.UserSignUp;
import com.demo.aws.domain.model.response.SuccessResponse;
import com.demo.aws.exceptions.FailedAuthenticationException;
import com.demo.aws.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Validated UserSignUp signUp) {
        UserType result = userService.createUser(signUp);
        return new ResponseEntity<>(new SuccessResponse(
                result,
                "User account created successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Validated Login loginRequest) {
        return new ResponseEntity<>(userService.authenticate(loginRequest), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.contains("Bearer ")) {
            String accessToken = bearerToken.replace("Bearer ", "");

            userService.logout(accessToken);

            return new ResponseEntity<>(new SuccessResponse(null, "Logout successfully"), HttpStatus.OK);
        }
        throw new FailedAuthenticationException("Invalid password.");
    }


}
