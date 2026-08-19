CREATE TABLE resultados_exame (
    id BIGINT NOT NULL AUTO_INCREMENT,
    exame_id BIGINT NOT NULL,
    data_resultado DATETIME NOT NULL,
    laudo VARCHAR(1000) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_resultado_exame_exame FOREIGN KEY (exame_id) REFERENCES exames (id),
    CONSTRAINT uk_resultado_exame_exame UNIQUE (exame_id)
    );