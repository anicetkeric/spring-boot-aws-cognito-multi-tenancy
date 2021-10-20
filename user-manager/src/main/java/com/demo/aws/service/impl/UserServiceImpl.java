package com.demo.aws.service.impl;

import com.amazonaws.services.cognitoidp.model.UserType;
import com.demo.aws.domain.TenantMaster;
import com.demo.aws.domain.model.Login;
import com.demo.aws.domain.model.UserSignUp;
import com.demo.aws.domain.model.response.SuccessResponse;
import com.demo.aws.service.TenantMasterService;
import com.demo.aws.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TenantMasterService tenantMasterService;


/*    @Override
    public void testDynamo() {
        Optional<TenantMaster> tenantMaster = tenantMasterService.getTenantById("616c3fb30f7a151e1362c79c");
        System.out.println("opp"+ tenantMaster.get());
    }*/

    @Override
    public SuccessResponse authenticate(Login userLogin) {
        return null;
    }

    @Override
    public void logout(@NotNull String accessToken) {

    }

    @Override
    public UserType createUser(UserSignUp signUp) {
        return null;
    }
}
