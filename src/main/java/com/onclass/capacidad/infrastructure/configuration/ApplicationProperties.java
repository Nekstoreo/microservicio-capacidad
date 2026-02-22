package com.onclass.capacidad.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Technology technology = new Technology();
    private Security security = new Security();
    private HttpClient httpClient = new HttpClient();

    @Data
    public static class Technology {
        private String baseUrl;
    }

    @Data
    public static class Security {
        private boolean enabled = true;
    }

    @Data
    public static class HttpClient {
        private Credentials credentials = new Credentials();

        @Data
        public static class Credentials {
            private String username;
            private String password;
        }
    }
}
