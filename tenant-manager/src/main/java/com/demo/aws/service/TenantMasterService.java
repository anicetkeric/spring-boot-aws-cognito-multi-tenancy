package com.demo.aws.service;

import com.demo.aws.domain.TenantMaster;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface TenantMasterService {

    /**
     * Get tenant info by id
     * @param tenantId tenant id
     * @return {{@link TenantMaster}}
     */
   Optional<TenantMaster> getTenantById(@NotNull String tenantId);
}
