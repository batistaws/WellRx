CREATE TABLE estoques (
    id BIGINT NOT NULL AUTO_INCREMENT,
    medicamento_id BIGINT NOT NULL,
    quantidade_disponivel INT NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_estoque_medicamento FOREIGN KEY (medicamento_id) REFERENCES medicamentos (id),
    CONSTRAINT uk_estoque_medicamento UNIQUE (medicamento_id)
    );