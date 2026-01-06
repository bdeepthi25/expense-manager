package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Auditors;

@Repository
public interface AuditorRepository extends JpaRepository<Auditors, Long>{

   boolean	existsByUser_IdAndIsActiveTrue(Long user_id);
}
