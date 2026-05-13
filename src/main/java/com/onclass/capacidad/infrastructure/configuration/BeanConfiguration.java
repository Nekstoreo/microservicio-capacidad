package com.onclass.capacidad.infrastructure.configuration;

import com.onclass.capacidad.domain.api.CreateCapabilityServicePort;
import com.onclass.capacidad.domain.api.DeleteCapabilityServicePort;
import com.onclass.capacidad.domain.api.ListCapabilitiesServicePort;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import com.onclass.capacidad.domain.spi.TechnologyManagementPort;
import com.onclass.capacidad.domain.usecases.CreateCapabilityUseCase;
import com.onclass.capacidad.domain.usecases.DeleteCapabilityUseCase;
import com.onclass.capacidad.domain.usecases.ListCapabilitiesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateCapabilityServicePort createCapabilityServicePort(
            CapabilityRepositoryPort capabilityRepositoryPort,
            TechnologyCatalogPort technologyCatalogPort) {
        return new CreateCapabilityUseCase(capabilityRepositoryPort, technologyCatalogPort);
    }

    @Bean
    public DeleteCapabilityServicePort deleteCapabilityServicePort(
            CapabilityRepositoryPort capabilityRepositoryPort,
            TechnologyManagementPort technologyManagementPort) {
        return new DeleteCapabilityUseCase(capabilityRepositoryPort, technologyManagementPort);
    }

    @Bean
    public ListCapabilitiesServicePort listCapabilitiesServicePort(
            CapabilityRepositoryPort capabilityRepositoryPort,
            TechnologyCatalogPort technologyCatalogPort) {
        return new ListCapabilitiesUseCase(capabilityRepositoryPort, technologyCatalogPort);
    }
}
