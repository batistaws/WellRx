CREATE TABLE prescricoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    atendimento_id BIGINT NOT NULL,
    data_emissao DATETIME NOT NULL,
    observacoes VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_prescricao_atendimento FOREIGN KEY (atendimento_id) REFERENCES atendimentos (id)
    );