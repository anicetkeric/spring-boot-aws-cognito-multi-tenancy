package com.demo.aws.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBTable(tableName = "tenant_master")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantMaster {

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String userPoolId;

    @DynamoDBAttribute
    private String appClientId;

    @DynamoDBAttribute
    private String appSecretId;

    @DynamoDBAttribute
    private String companyKey;

    @DynamoDBAttribute
    private boolean status;

}


