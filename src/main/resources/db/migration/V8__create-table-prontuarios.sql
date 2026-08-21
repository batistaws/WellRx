        CREATE TABLE prontuarios (
        id BIGINT NOT NULL AUTO_INCREMENT,
        paciente_id BIGINT NOT NULL,
        data_criacao DATE NOT NULL,

        PRIMARY KEY (id),
        CONSTRAINT fk_prontuario_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes (id),
        CONSTRAINT uk_prontuario_paciente UNIQUE (paciente_id)
        );
