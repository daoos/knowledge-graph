package com.beyond.algm.algmalgorithmsboot.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="publish")
@Data
public class PublishConfigModel {
    private String localBasePath;
    private String distFolder;
    private String javaVersion;
    private String packageName;
}
