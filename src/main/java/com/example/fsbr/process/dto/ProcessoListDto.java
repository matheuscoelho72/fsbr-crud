package com.example.fsbr.process.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessoListDto {

    private Long id;
    private String npu;
    private LocalDate dataCadastro;
    private String uf;

}
