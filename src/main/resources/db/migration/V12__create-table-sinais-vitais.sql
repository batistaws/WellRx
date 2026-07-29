CREATE TABLE sinais_vitais (
    id BIGINT NOT NULL AUTO_INCREMENT,
    atendimento_id BIGINT NOT NULL,
    peso DOUBLE NOT NULL,
    altura DOUBLE NOT NULL,
    pressao_arterial VARCHAR(10) NOT NULL,
    temperatura DOUBLE NOT NULL,
    frequencia_cardiaca INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_sinais_vitais_atendimento FOREIGN KEY (atendimento_id) REFERENCES atendimentos (id),
    CONSTRAINT uk_sinais_vitais_atendimento UNIQUE (atendimento_id)
    );
