CREATE TABLE IF NOT EXISTS capabilities
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    name
    VARCHAR
(
    150
) NOT NULL,
    description TEXT NOT NULL,
    CONSTRAINT uk_capabilities_name UNIQUE
(
    name
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS capability_technologies
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    capability_id
    BIGINT
    NOT
    NULL,
    technology_id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    fk_capability_technologies_capability
    FOREIGN
    KEY
(
    capability_id
)
    REFERENCES capabilities
(
    id
)
    ON DELETE CASCADE,
    CONSTRAINT uk_capability_technology UNIQUE
(
    capability_id,
    technology_id
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_unicode_ci;

CREATE INDEX idx_capability_technologies_capability_id ON capability_technologies (capability_id);
CREATE INDEX idx_capability_technologies_technology_id ON capability_technologies (technology_id);
