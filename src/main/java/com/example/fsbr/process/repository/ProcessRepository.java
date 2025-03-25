package com.example.fsbr.process.repository;

import com.example.fsbr.process.filter.ProcessFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProcessRepository extends JpaRepository<Process, Integer> {

    @Query("from Process where id = :id")
    Optional<Process> findById(@Param("id") Long id);

    Page<com.example.fsbr.process.model.Process> findByFilter(ProcessFilter filter, Pageable pageable);
}
