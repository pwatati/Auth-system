package com.security.authentication.repository;

import com.security.authentication.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByIdNumberAndCode(Long idNumber, String code);
    void deleteByIdNumber(Long idNumber);
}