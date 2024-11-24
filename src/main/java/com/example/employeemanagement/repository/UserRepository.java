package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.otp = :otp, u.otpExpireTime = :expireTime, u.failedOtpAttempts = 0 WHERE u.id = :userId")
    void updateUserOtp(@Param("userId") Long userId,
            @Param("otp") String otp,
            @Param("expireTime") LocalDateTime expireTime);

    @Modifying
    @Query("UPDATE User u SET u.failedOtpAttempts = u.failedOtpAttempts + 1 WHERE u.id = :userId")
    void incrementFailedOtpAttempts(@Param("userId") Long userId);

}
