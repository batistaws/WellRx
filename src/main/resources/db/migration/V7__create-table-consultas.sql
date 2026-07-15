CREATE TABLE consultas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medico_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    motivo_consulta VARCHAR(255),
    data_consulta DATETIME NOT NULL,
    status_consulta VARCHAR(20) NOT NULL,
    motivo_cancelamento VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES medicos (id),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes (id)
    );