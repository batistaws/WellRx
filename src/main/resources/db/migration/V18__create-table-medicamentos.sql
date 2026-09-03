CREATE TABLE medicamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    principio_ativo VARCHAR(255),
    controlado BOOLEAN NOT NULL DEFAULT FALSE,
    unidade_medida VARCHAR(50) NOT NULL,

    PRIMARY KEY (id)
    );