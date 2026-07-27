CREATE TABLE atendimentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    prontuario_id BIGINT NOT NULL,
    consulta_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_atendimento DATETIME NOT NULL,
    queixa VARCHAR(255) NOT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    condutas VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_atendimento_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuarios (id),
    CONSTRAINT fk_atendimento_consulta FOREIGN KEY (consulta_id) REFERENCES consultas (id),
    CONSTRAINT uk_atendimento_consulta UNIQUE (consulta_id),
    CONSTRAINT fk_atendimento_medico FOREIGN KEY (medico_id) REFERENCES medicos (id)
    );