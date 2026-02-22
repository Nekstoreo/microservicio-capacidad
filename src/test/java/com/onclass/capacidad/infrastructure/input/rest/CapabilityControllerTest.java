package com.onclass.capacidad.infrastructure.input.rest;

import com.onclass.capacidad.application.port.in.CreateCapabilityUseCase;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CreateCapabilityUseCase createCapabilityUseCase;

    @BeforeEach
    void setUp() {
        CapabilityController controller = new CapabilityController(createCapabilityUseCase);
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
}
