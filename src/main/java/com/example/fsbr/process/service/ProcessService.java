package com.example.fsbr.process.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessService{

    private final ProductDepositRepository repository;
    private final ProductDepositRepositoryCustom repositoryCustom;
    private final ProductDepositMapper mapper;

}
