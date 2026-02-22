package com.onclass.capacidad.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Technology technology = new Technology();

    @Data
    public static class Technology {
        private String baseUrl;
    }
}
