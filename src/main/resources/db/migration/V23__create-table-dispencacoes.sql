CREATE TABLE dispensacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    item_prescricao_id BIGINT NOT NULL,
    farmaceutico_id BIGINT NOT NULL,
    quantidade_dispensada INT NOT NULL,
    data_dispensacao DATETIME NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_dispensacao_item_prescricao FOREIGN KEY (item_prescricao_id) REFERENCES itens_prescricao (id),
    CONSTRAINT uk_dispensacao_item_prescricao UNIQUE (item_prescricao_id),
    CONSTRAINT fk_dispensacao_farmaceutico FOREIGN KEY (farmaceutico_id) REFERENCES farmaceuticos (id)
    );