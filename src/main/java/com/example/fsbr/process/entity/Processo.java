package com.example.fsbr.process.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O NPU é obrigatório.")
    @Pattern(regexp = "\\d{7}-\\d{2}\\.\\d{4}\\.\\d\\.\\d{2}\\.\\d{4}",
            message = "O NPU deve estar no formato 1111111-11.1111.1.11.1111")
    private String npu;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataCadastro;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dataVisualizacao;

    @NotBlank(message = "O Município é obrigatório.")
    private String municipio;

    @NotBlank(message = "A UF é obrigatória.")
    private String uf;

    private String documentoPdf; // Nome do arquivo PDF

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDate.now();
    }
}
