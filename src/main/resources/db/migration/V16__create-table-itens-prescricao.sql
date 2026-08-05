CREATE TABLE itens_prescricao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    prescricao_id BIGINT NOT NULL,
    medicamento VARCHAR(255) NOT NULL,
    dosagem VARCHAR(100) NOT NULL,
    posologia VARCHAR(255) NOT NULL,
    duracao_dias INT,

    PRIMARY KEY (id),
    CONSTRAINT fk_item_prescricao_prescricao FOREIGN KEY (prescricao_id) REFERENCES prescricoes (id)
    );