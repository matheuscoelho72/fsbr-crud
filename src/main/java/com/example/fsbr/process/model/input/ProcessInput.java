package com.example.fsbr.process.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessInput {

    @JsonProperty("processId")
    private Long id;

    @JsonProperty("npu")
    private String npu;

    @JsonProperty("createDate")
    private LocalDate createDate;

    @JsonProperty("visualizationDate")
    private LocalDate visualizationDate;

    @JsonProperty("city")
    private String city;

    @JsonProperty("uf")
    private String uf;

    @JsonProperty("uploadPdf")
    private String uploadPdf;

}
