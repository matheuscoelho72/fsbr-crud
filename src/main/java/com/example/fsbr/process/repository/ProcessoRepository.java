package com.example.fsbr.process.repository;

import com.example.fsbr.process.entity.Processo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {

    Page<Processo> findAll(Pageable pageable);
}