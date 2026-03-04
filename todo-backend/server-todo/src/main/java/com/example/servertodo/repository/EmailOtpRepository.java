package com.example.servertodo.repository;

import com.example.servertodo.entity.AppUser;
import com.example.servertodo.entity.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOtp, Integer> {

    Optional<EmailOtp> findTopByUserAndIsUsedFalseOrderByIdDesc(AppUser user);
}
