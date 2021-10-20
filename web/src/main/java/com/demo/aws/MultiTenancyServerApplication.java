package com.demo.aws;

import com.demo.aws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiTenancyServerApplication implements CommandLineRunner {

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(MultiTenancyServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userService.testDynamo();
        //AwsConfig awsConfig = new AwsConfig();
       // System.out.println("access key: " +tenantMasterService.getTenantById("tenant 1"));
    }
}
