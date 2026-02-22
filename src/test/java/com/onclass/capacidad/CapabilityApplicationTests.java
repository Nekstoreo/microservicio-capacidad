package com.onclass.capacidad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class CapabilityApplicationTests {

    @MockitoBean
    private com.onclass.capacidad.domain.spi.TechnologyCatalogPort technologyCatalogPort;

    @MockitoBean
    private com.onclass.capacidad.domain.spi.TechnologyManagementPort technologyManagementPort;

    @Test
    void contextLoads() {
        // Test that context loads successfully
    }
}
