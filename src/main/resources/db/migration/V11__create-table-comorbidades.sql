CREATE TABLE comorbidades (
    id BIGINT NOT NULL AUTO_INCREMENT,
    prontuario_id BIGINT NOT NULL,
    condicao VARCHAR(255) NOT NULL,
    data_diagnostico DATE,

    PRIMARY KEY (id),
    CONSTRAINT fk_comorbidade_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuarios (id)
    );