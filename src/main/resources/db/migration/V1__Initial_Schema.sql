-- Tabla inicial para Capacidades
CREATE TABLE IF NOT EXISTS capacidades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador único de capacidad',
    nombre VARCHAR(150) NOT NULL UNIQUE COMMENT 'Nombre de la capacidad',
    descripcion TEXT COMMENT 'Descripción detallada de la capacidad',
    nivel ENUM('BASICO', 'INTERMEDIO', 'AVANZADO', 'EXPERTO') NOT NULL DEFAULT 'BASICO' COMMENT 'Nivel de dificultad de la capacidad',
    estado ENUM('ACTIVO', 'INACTIVO', 'DEPRECADO') NOT NULL DEFAULT 'ACTIVO' COMMENT 'Estado de la capacidad',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    CONSTRAINT uk_capacidad_nombre UNIQUE KEY (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Entidad de Capacidades del sistema';

-- Índices para optimizar búsquedas
CREATE INDEX idx_capacidades_nivel ON capacidades(nivel);
CREATE INDEX idx_capacidades_estado ON capacidades(estado);
