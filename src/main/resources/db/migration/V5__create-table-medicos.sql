CREATE TABLE medicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_completo VARCHAR(255) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE,
    especialidade VARCHAR(100),
    sexo VARCHAR(20),

    logradouro VARCHAR(255),
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    cep VARCHAR(20),
    complemento VARCHAR(255),
    estado VARCHAR(2),
    usuario_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_medicos_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

