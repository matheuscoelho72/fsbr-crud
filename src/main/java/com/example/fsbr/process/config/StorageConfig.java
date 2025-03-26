package com.example.fsbr.process.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("file.upload")
@Getter
@Setter
public class StorageConfig {

    private String location = "uploads";

}
