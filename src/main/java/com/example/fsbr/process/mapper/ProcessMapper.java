package com.example.fsbr.process.mapper;

import com.example.fsbr.process.model.Process;
import com.example.fsbr.process.model.ProcessModel;

import java.util.List;

public class ProcessMapper {

    public static ProcessModel toModel(Process entity) {
        return ProcessModel.builder()
                .npu(entity.getNpu())
                .createDate(entity.getCreateDate())
                .uf(entity.getUf())
                .build();
    }

    public static List<ProcessModel> toModelList(List<Process> entities) {
        return entities.stream().map(item -> toModel(item)).toList();
    }

}
