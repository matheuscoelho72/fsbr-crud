package com.example.fsbr.process.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

/* Representa o Modelo do Processo CRUD FSBR */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessModel {

    @JsonProperty("npu")
    private String npu;

    @JsonProperty("createDate")
    private LocalDate createDate;

    @JsonProperty("uf")
    private String uf;

}
