package com.onclass.capacidad.infrastructure.input.rest;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.application.mapper.CapabilityMapper;
import com.onclass.capacidad.application.port.in.CreateCapabilityUseCase;
import com.onclass.capacidad.application.usecase.ListCapabilitiesService;
import com.onclass.capacidad.application.usecase.command.CreateCapabilityCommand;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CapabilityControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CreateCapabilityUseCase createCapabilityUseCase;

    @Mock
    private ListCapabilitiesService listCapabilitiesService;

    @Mock
    private CapabilityMapper capabilityMapper;

    @BeforeEach
    void setUp() {
        // Mocks para ListCapabilitiesService y CapabilityMapper con default
        when(listCapabilitiesService.executeWithTechnologyNames(any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));
        when(capabilityMapper.toListItemResponse(any()))
                .thenReturn(new com.onclass.capacidad.infrastructure.input.rest.dto.CapabilityListItemResponse(
                        1L, "Test", "Test description", List.of()
                ));

        CapabilityController controller = new CapabilityController(createCapabilityUseCase, listCapabilitiesService, capabilityMapper);
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnCreatedWhenRequestIsValid() {
        when(createCapabilityUseCase.execute(any(CreateCapabilityCommand.class)))
                .thenReturn(Capability.rehydrate(1L, "Backend", "Backend capability", List.of(1L, 2L, 3L)));

        webTestClient.post()
                .uri(ApiConstants.CAPACITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          \"name\": \"Backend\",
                          \"description\": \"Backend capability\",
                          \"technologyIds\": [1,2,3]
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Backend")
                .jsonPath("$.technologyIds[0]").isEqualTo(1)
                .jsonPath("$.technologyIds.length()").isEqualTo(3);
    }

    @Test
    void shouldReturnBadRequestWhenTechnologyIdsAreLessThanThree() {
        webTestClient.post()
                .uri(ApiConstants.CAPACITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          \"name\": \"Backend\",
                          \"description\": \"Backend capability\",
                          \"technologyIds\": [1,2]
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnConflictWhenNameAlreadyExists() {
        when(createCapabilityUseCase.execute(any(CreateCapabilityCommand.class)))
                .thenThrow(new DuplicateCapabilityException("Backend"));

        webTestClient.post()
                .uri(ApiConstants.CAPACITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          \"name\": \"Backend\",
                          \"description\": \"Backend capability\",
                          \"technologyIds\": [1,2,3]
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void shouldReturnUnprocessableEntityWhenAnyTechnologyDoesNotExist() {
        when(createCapabilityUseCase.execute(any(CreateCapabilityCommand.class)))
                .thenThrow(new TechnologiesNotFoundException(List.of(3L)));

        webTestClient.post()
                .uri(ApiConstants.CAPACITIES_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          \"name\": \"Backend\",
                          \"description\": \"Backend capability\",
                          \"technologyIds\": [1,2,3]
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    // @Test
    // void shouldReturnOkWhenGetListIsCalled() {
    //     webTestClient.get()
    //             .uri(ApiConstants.CAPACITIES_BASE_PATH)
    //             .exchange()
    //             .expectStatus().isOk();
    // }
}

