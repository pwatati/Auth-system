package com.security.authentication.repository;

import com.security.authentication.model.LoginCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginCodeRepository extends JpaRepository<LoginCode, Long> {
    Optional<LoginCode> findByIdNumberAndCode(Long idNumber, String code);
    void deleteByIdNumber(Long idNumber); }