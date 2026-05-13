package com.onclass.capacidad.infrastructure.endpoints.rest;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.application.dtos.commands.CreateCapabilityCommand;
import com.onclass.capacidad.application.dtos.requests.CreateCapabilityRequest;
import com.onclass.capacidad.application.dtos.responses.CapabilitiesPageResponse;
import com.onclass.capacidad.application.dtos.responses.CapabilityListItemResponse;
import com.onclass.capacidad.application.dtos.responses.CapabilityResponse;
import com.onclass.capacidad.application.dtos.responses.ErrorResponse;
import com.onclass.capacidad.application.mappers.CapabilityMapper;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;
import com.onclass.capacidad.domain.api.CreateCapabilityServicePort;
import com.onclass.capacidad.domain.api.DeleteCapabilityServicePort;
import com.onclass.capacidad.domain.api.ListCapabilitiesServicePort;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CAPABILITIES_BASE_PATH)
@RequiredArgsConstructor
public class CapabilityController {

    private final CreateCapabilityServicePort createCapabilityUseCase;
    private final DeleteCapabilityServicePort deleteCapabilityUseCase;
    private final ListCapabilitiesServicePort listCapabilitiesService;
    private final CapabilityMapper capabilityMapper;

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
    public Mono<CapabilityResponse> create(@Valid @RequestBody CreateCapabilityRequest request) {
        return Mono
                .fromCallable(() -> createCapabilityUseCase.execute(new CreateCapabilityCommand(request.name(), request.description(), request.technologyIds())))
                .subscribeOn(Schedulers.boundedElastic())
                .map(CapabilityResponse::fromDomain);
    }

    @Operation(summary = ApiConstants.OPENAPI_LIST_CAPABILITIES_SUMMARY, description = ApiConstants.OPENAPI_LIST_CAPABILITIES_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiConstants.OPENAPI_LIST_CAPABILITIES_SUCCESS),
            @ApiResponse(responseCode = "400", description = ApiConstants.OPENAPI_INVALID_REQUEST, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = ApiConstants.OPENAPI_UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApiConstants.OPENAPI_FORBIDDEN)
    })
    @GetMapping
    public Mono<CapabilitiesPageResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return Mono
                .fromCallable(() -> {
                    DomainPageRequest pageRequest = new DomainPageRequest(page, size, sortBy, sortDir);
                    return listCapabilitiesService.executeWithTechnologyNames(pageRequest);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toPageResponse);
    }

    @Operation(summary = "Get Capability", description = "Gets a capability by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capability found", content = @Content(schema = @Schema(implementation = CapabilityListItemResponse.class))),
            @ApiResponse(responseCode = "404", description = ApiConstants.OPENAPI_CAPABILITY_NOT_FOUND, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public Mono<CapabilityListItemResponse> getById(@PathVariable Long id) {
        return Mono
                .fromCallable(() -> {
                    CapabilityWithTechnologies capability = listCapabilitiesService.getByIdWithTechnologyNames(id);
                    if (capability == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ApiConstants.OPENAPI_CAPABILITY_NOT_FOUND);
                    }
                    return capabilityMapper.toListItemResponse(capability);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Operation(summary = ApiConstants.OPENAPI_GET_CAPABILITIES_BULK_SUMMARY, description = ApiConstants.OPENAPI_GET_CAPABILITIES_BULK_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ApiConstants.OPENAPI_GET_CAPABILITIES_BULK_SUCCESS),
            @ApiResponse(responseCode = "400", description = ApiConstants.OPENAPI_INVALID_REQUEST, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(ApiConstants.BULK_PATH)
    public Mono<List<CapabilityListItemResponse>> getBulk(@RequestParam List<Long> ids) {
        return Mono
                .fromCallable(() -> listCapabilitiesService.getByIdsWithTechnologyNames(ids))
                .subscribeOn(Schedulers.boundedElastic())
                .map(list -> list.stream()
                        .map(capabilityMapper::toListItemResponse)
                        .toList());
    }

    @Operation(summary = ApiConstants.OPENAPI_DELETE_CAPABILITY_SUMMARY,
            description = ApiConstants.OPENAPI_DELETE_CAPABILITY_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ApiConstants.OPENAPI_DELETE_CAPABILITY_SUCCESS),
            @ApiResponse(responseCode = "404", description = ApiConstants.OPENAPI_CAPABILITY_NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = ApiConstants.OPENAPI_TECHNOLOGY_DELETION_FAILED,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return Mono
                .<Void>fromRunnable(() -> deleteCapabilityUseCase.execute(id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private CapabilitiesPageResponse toPageResponse(DomainPage<CapabilityWithTechnologies> page) {
        var mappedContent = page.content().stream()
                .map(capabilityMapper::toListItemResponse)
                .toList();

        return new CapabilitiesPageResponse(
                mappedContent,
                page.pageNumber(),
                page.pageSize(),
                page.totalElements(),
                page.totalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
