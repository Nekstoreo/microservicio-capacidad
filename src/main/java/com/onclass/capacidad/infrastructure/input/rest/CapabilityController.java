package com.onclass.capacidad.infrastructure.input.rest;

import com.onclass.capacidad.application.port.in.CreateCapabilityUseCase;
import com.onclass.capacidad.application.usecase.command.CreateCapabilityCommand;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import com.onclass.capacidad.infrastructure.input.rest.dto.CapabilityResponse;
import com.onclass.capacidad.infrastructure.input.rest.dto.CreateCapabilityRequest;
import com.onclass.capacidad.infrastructure.input.rest.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.CAPACITIES_BASE_PATH)
@RequiredArgsConstructor
public class CapabilityController {

    private final CreateCapabilityUseCase createCapabilityUseCase;

    @Operation(summary = ApiConstants.OPENAPI_CREATE_CAPABILITY_SUMMARY, description = ApiConstants.OPENAPI_CREATE_CAPABILITY_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ApiConstants.OPENAPI_CAPABILITY_CREATED),
            @ApiResponse(responseCode = "400", description = ApiConstants.OPENAPI_INVALID_REQUEST, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = ApiConstants.OPENAPI_UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApiConstants.OPENAPI_FORBIDDEN),
            @ApiResponse(responseCode = "409", description = ApiConstants.OPENAPI_DUPLICATE_CAPABILITY, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = ApiConstants.OPENAPI_TECHNOLOGIES_NOT_FOUND, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = ApiConstants.OPENAPI_TECHNOLOGY_CATALOG_UNAVAILABLE, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public reactor.core.publisher.Mono<CapabilityResponse> create(@Valid @RequestBody CreateCapabilityRequest request) {
        return reactor.core.publisher.Mono
                .fromCallable(() -> createCapabilityUseCase.execute(new CreateCapabilityCommand(request.name(), request.description(), request.technologyIds())))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
                .map(CapabilityResponse::fromDomain);
    }
}
