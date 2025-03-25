package com.example.fsbr.process.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProcessFilter {

    private Long id;
    private String npu;
    private LocalDate createDate;
    private LocalDate visualizationDate;
    private String city;
    private String uf;

}
