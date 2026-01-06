package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ChargeCode;

@Repository
public interface ChargeCodeRepository extends JpaRepository<ChargeCode, Long>{


    ChargeCode findByChargeCode(String chargeCode);
}
