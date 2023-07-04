package com.MayankApp.repository;

import com.MayankApp.entity.UserDetails;
import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetails,Integer> {
    UserDetails findByEmail(String email);
    UserDetails findByEmailAndEnabled(String email,boolean enabled);
    UserDetails findByName(String name);
    UserDetails findByEmailAndPassword(String email, String password);

    void deleteById(Long id);

    Optional<UserDetails> findById(Long id);

    @Query("select u from UserDetails u where u.verificationCode= ?1")
    UserDetails findByVerificationCode(String code);
}


