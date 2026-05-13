package com.onclass.capacidad.infrastructure.endpoints.rest;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.application.dtos.requests.CreateCapabilityRequest;
import com.onclass.capacidad.application.dtos.responses.CapabilityListItemResponse;
import com.onclass.capacidad.application.mappers.CapabilityMapper;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.api.CreateCapabilityServicePort;
import com.onclass.capacidad.domain.api.DeleteCapabilityServicePort;
import com.onclass.capacidad.domain.api.ListCapabilitiesServicePort;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import com.onclass.capacidad.infrastructure.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CreateCapabilityServicePort createCapabilityUseCase;

    @Mock
    private DeleteCapabilityServicePort deleteCapabilityUseCase;

    @Mock
    private ListCapabilitiesServicePort listCapabilitiesUseCase;

    @Mock
    private CapabilityMapper capabilityMapper;

    @InjectMocks
    private CapabilityController capabilityController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(capabilityController)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should return 201 Created when capability is created successfully")
    void shouldReturn201CreatedWhenCapabilityIsCreated() {
        CreateCapabilityRequest request = new CreateCapabilityRequest("Java", "Java Desc", List.of(1L, 2L, 3L));
        Capability capability = Capability.rehydrate(1L, "Java", "Java Desc", List.of(1L, 2L, 3L));

        when(createCapabilityUseCase.execute(any())).thenReturn(capability);

        webTestClient.post()
                .uri(ApiConstants.CAPABILITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Java");
    }

    @Test
    @DisplayName("Should return 409 Conflict when capability name already exists")
    void shouldReturn409ConflictWhenNameExists() {
        CreateCapabilityRequest request = new CreateCapabilityRequest("Java", "Java Desc", List.of(1L, 2L, 3L));

        when(createCapabilityUseCase.execute(any())).thenThrow(new DuplicateCapabilityException("Java"));

        webTestClient.post()
                .uri(ApiConstants.CAPABILITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    @DisplayName("Should return 200 OK when listing capabilities")
    void shouldReturn200OKWhenListing() {
        CapabilityWithTechnologies dto = new CapabilityWithTechnologies(1L, "Java", "Desc", List.of());
        when(listCapabilitiesUseCase.executeWithTechnologyNames(any())).thenReturn(new DomainPage<>(List.of(dto), 0, 20, 1, 1, false, false));
        when(capabilityMapper.toListItemResponse(any()))
                .thenReturn(new CapabilityListItemResponse(1L, "Java", "Desc", List.of()));

        webTestClient.get()
                .uri(ApiConstants.CAPABILITIES_BASE_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo(1);
    }

    @Test
    @DisplayName("Should return 204 No Content when deleting capability")
    void shouldReturn204NoContentWhenDeleting() {
        Long id = 1L;
        doNothing().when(deleteCapabilityUseCase).execute(id);

        webTestClient.delete()
                .uri(ApiConstants.CAPABILITIES_BASE_PATH + "/{id}", id)
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteCapabilityUseCase).execute(id);
    }
}
