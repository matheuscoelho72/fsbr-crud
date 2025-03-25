package com.example.fsbr.process.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Data
@Table(name = "process")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "npu")
    private String npu; //1111111-11.1111.1.11.1111

    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "visualizationDate")
    private LocalDate visualizationDate;

    @Column(name = "city")
    private String city;

    @Column(name = "uf")
    private String uf;

    @Column(name = "uploadPdf", length = 5000)
    private String uploadPdf;

}
