package com.demo.aws.service;

import com.demo.aws.domain.TenantMaster;
import com.demo.aws.repository.TenantMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Slf4j
public class TenantMasterServiceImpl implements TenantMasterService {

   private final TenantMasterRepository repository;

    public TenantMasterServiceImpl(TenantMasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TenantMaster> getTenantById(@NotNull String tenantId) {
        return repository.findById(tenantId);
    }
}
