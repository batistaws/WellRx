CREATE TABLE alergias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    prontuario_id BIGINT NOT NULL,
    substancia VARCHAR(255) NOT NULL,
    gravidade VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_alergia_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuarios (id)
    );
