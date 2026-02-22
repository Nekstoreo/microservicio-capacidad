package com.onclass.capacidad.infrastructure.constants;

public final class ApiConstants {

    public static final String CAPABILITIES_BASE_PATH = "/capabilities";

    public static final String OPENAPI_CREATE_CAPABILITY_SUMMARY = "Create capability";
    public static final String OPENAPI_CREATE_CAPABILITY_DESCRIPTION = "Creates a capability with unique name and associated technologies";
    public static final String OPENAPI_CAPABILITY_CREATED = "Capability created successfully";
    public static final String OPENAPI_INVALID_REQUEST = "Invalid request";
    public static final String OPENAPI_UNAUTHORIZED = "Unauthorized";
    public static final String OPENAPI_FORBIDDEN = "Forbidden";
    public static final String OPENAPI_DUPLICATE_CAPABILITY = "Duplicate capability name";
    public static final String OPENAPI_TECHNOLOGIES_NOT_FOUND = "One or more technologies were not found";
    public static final String OPENAPI_TECHNOLOGY_CATALOG_UNAVAILABLE = "Technology catalog unavailable";

    public static final String VALIDATION_NAME_REQUIRED = "Name is required";
    public static final String VALIDATION_DESCRIPTION_REQUIRED = "Description is required";
    public static final String VALIDATION_TECHNOLOGY_IDS_REQUIRED = "technologyIds is required";
    public static final String VALIDATION_TECHNOLOGIES_SIZE = "Capability must include between 3 and 20 technologies";
    public static final String VALIDATION_TECHNOLOGY_ID_REQUIRED = "Technology id is required";

    public static final String DEFAULT_INVALID_REQUEST_MESSAGE = "Invalid request";
    public static final String TECHNOLOGY_EXISTS_PATH = "/technologies/exists";
    public static final String TECHNOLOGY_EXISTS_QUERY_PARAM = "ids";
    public static final String TECHNOLOGY_CATALOG_UNAVAILABLE_MESSAGE = "Technology catalog is unavailable";

    public static final String OPENAPI_LIST_CAPABILITIES_SUMMARY = "List capabilities";
    public static final String OPENAPI_LIST_CAPABILITIES_DESCRIPTION = "List all capabilities with pagination and sorting support";
    public static final String OPENAPI_LIST_CAPABILITIES_SUCCESS = "Capabilities listed successfully";

    public static final String OPENAPI_GET_CAPABILITIES_BULK_SUMMARY = "Get capabilities by IDs";
    public static final String OPENAPI_GET_CAPABILITIES_BULK_DESCRIPTION = "Get details of multiple capabilities by their IDs";
    public static final String OPENAPI_GET_CAPABILITIES_BULK_SUCCESS = "Capabilities retrieved successfully";

    public static final String BULK_PATH = "/bulk";

    private ApiConstants() {
    }
}
