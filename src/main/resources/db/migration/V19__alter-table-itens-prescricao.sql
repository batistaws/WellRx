ALTER TABLE itens_prescricao ADD COLUMN medicamento_id BIGINT;
ALTER TABLE itens_prescricao ADD CONSTRAINT fk_item_prescricao_medicamento
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos (id);