CREATE TABLE exames (
    id BIGINT NOT NULL AUTO_INCREMENT,
    atendimento_id BIGINT NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_solicitacao DATETIME NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_exame_atendimento FOREIGN KEY (atendimento_id) REFERENCES atendimentos (id)
    );
