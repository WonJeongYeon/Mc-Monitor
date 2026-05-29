package com.example.mc_monitor.repository;

import com.example.mc_monitor.entity.Audit;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends R2dbcRepository<Audit, Integer> {

}
