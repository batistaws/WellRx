# WellRx

## 📋 Descrição do Projeto

O **WellRx** é uma API backend desenvolvida em **Java com Spring Boot** para gestão completa de uma clínica médica — do agendamento da consulta até a dispensação do medicamento na farmácia. O projeto foi construído com foco em **arquitetura em camadas**, **modelagem de domínio orientada a regras de negócio reais** e **segurança aplicada em múltiplas camadas** (autenticação, autorização por hierarquia de perfis e controle de posse sobre os dados).

O sistema cobre o fluxo clínico de ponta a ponta:

```
Paciente agenda Consulta → Médico realiza Atendimento → gera Prescrição / solicita Exame
        → Farmacêutico dispensa o medicamento, com baixa automática em Estoque
```

---

## 🧩 Módulos

### 🏥 Clínica
Cadastro e gestão de **Pacientes**, **Médicos** e **Recepcionistas**, cada um com autenticação própria via `Usuario`. Agendamento de **Consultas**, com validações de negócio encadeadas (Strategy Pattern): antecedência mínima, horário de funcionamento, conflito de agenda do médico e do paciente, status ativo do profissional.

### 📁 Prontuário
Cada paciente possui um **Prontuário** único, criado automaticamente no cadastro. Nele são registrados:
- **Atendimentos** — um por consulta realizada, com queixa, diagnóstico e condutas
- **Sinais Vitais** — peso, altura, pressão arterial, temperatura e frequência cardíaca de cada atendimento
- **Alergias** e **Comorbidades** — histórico permanente do paciente

### 🔬 Exame
Fluxo de solicitação de exames vinculado ao atendimento, com máquina de estados (`AGENDADO → REALIZADO / CANCELADO`) e registro de laudo em `ResultadoExame`.

### 💊 Prescrição
Receituário médico emitido a partir de um atendimento, com múltiplos itens (`ItemPrescricao`), cada um referenciando um **Medicamento** cadastrado — não texto livre — o que permite rastreabilidade e controle de estoque real.

### 🏪 Farmácia
- **Medicamento** — catálogo central (nome, princípio ativo, se é controlado, unidade de medida)
- **Estoque** — saldo disponível por medicamento, com reposição controlada
- **Farmacêutico** — perfil de acesso próprio, responsável pela dispensação
- **Dispensação** — baixa de um item de prescrição, com validação de estoque suficiente e trava contra dispensação duplicada do mesmo item (constraint `UNIQUE` no banco)

---

## 🔐 Segurança

Segurança foi tratada como requisito de primeira classe, não como camada adicionada por último — especialmente relevante aqui, já que o sistema lida com **dado de saúde**, categorizado como dado sensível pela LGPD (Art. 5º, II).

### Autenticação
- **JWT stateless** — nenhuma sessão é mantida em servidor (`SessionCreationPolicy.STATELESS`); cada requisição se autentica pelo próprio token.
- **Senhas com hash BCrypt** (`BCryptPasswordEncoder`) — nenhuma senha é armazenada em texto plano ou com algoritmos reversíveis. O BCrypt aplica *salt* automático por senha, mitigando ataques de rainbow table.
- Verificação de conta por e-mail no cadastro, com token de confirmação.

### Autorização
- **Controle de acesso centralizado** por rota + verbo HTTP em um único ponto (`ConfigSecurity`), em vez de anotações espalhadas por cada controller — facilita auditoria de "quem pode acessar o quê" no projeto inteiro.
- **Hierarquia de perfis** nativa do Spring Security (`RoleHierarchy`):
  ```
  ADMIN > MEDICO > RECEPCIONISTA > PACIENTE
  ADMIN > FARMACEUTICO
  ```
  Um perfil superior herda automaticamente as permissões dos perfis abaixo dele, sem duplicar regras.
- **Controle de posse (ownership) na camada de serviço** — a role sozinha não é suficiente em endpoints sensíveis: um médico só acessa os atendimentos que ele mesmo realizou; um paciente só vê o próprio prontuário, exames e prescrições. Essa checagem é feita comparando o usuário autenticado com o dono real do recurso, prevenindo **IDOR (Insecure Direct Object Reference)** — ou seja, impede que um usuário troque um ID na URL e acesse dados de outra pessoa.

### Integridade de dados
Regras de negócio críticas são reforçadas **também no nível do banco**, não só na aplicação — para que uma falha de validação na aplicação nunca resulte em dado inconsistente:
- `UNIQUE (consulta_id)` em `atendimentos` — uma consulta não pode gerar mais de um atendimento
- `UNIQUE (item_prescricao_id)` em `dispensacoes` — um item de receita não pode ser dispensado duas vezes
- `UNIQUE (paciente_id)` em `prontuarios` — um paciente não pode ter mais de um prontuário
- Foreign keys em todas as relações entre módulos, impedindo referências órfãs

### Prevenção a SQL Injection
Nenhuma query do projeto é construída por concatenação de string. Todo acesso a dados é feito via:
- **Spring Data JPA** com métodos derivados por nome (`findByPacienteId`, `findByUsuarioId`, etc.), que usam *bind parameters* internamente;
- **JPQL com parâmetros nomeados** (`:id`, `:data`) nas consultas customizadas (`@Query`);
- **Criteria API** (via `Specification`) nos filtros dinâmicos.

Em nenhum ponto do código há `Statement` puro ou `@Query(nativeQuery = true)` com valor de entrada concatenado diretamente na string SQL — a superfície de ataque para SQL Injection é, portanto, nula na versão atual.

---

## 🧪 Tecnologias e Ferramentas

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | MySQL 8 |
| Migração de banco | Flyway |
| Segurança | Spring Security + JWT + BCrypt |
| Validação | Jakarta Bean Validation |
| Documentação da API | Swagger / OpenAPI |
| Build | Maven |
| Utilitários | Lombok |
| Containerização | Docker (build multi-stage) |
| Cloud / Deploy | AWS Elastic Beanstalk, AWS RDS |

---

## 📁 Estrutura do Projeto

```
WellRx/
│
├── clinica/          → Paciente, Médico, Recepcionista, Consulta
├── prontuario/        → Prontuario, Atendimento, SinaisVitais, Alergia, Comorbidade, Exame
├── prescricao/        → Prescricao, ItemPrescricao
├── farmacia/          → Medicamento, Estoque, Farmaceutico, Dispensacao
├── shared/            → Usuario, autenticação e recursos comuns entre módulos
└── infra/             → Segurança (JWT, filtros, hierarquia de perfis), tratamento global de exceções, e-mail
```

Cada módulo segue a mesma organização interna, separando claramente as responsabilidades:

```
modulo/
├── controller/        → Endpoints REST
├── service/           → Regras de negócio e orquestração
├── database/
│   ├── model/          → Entidades JPA
│   └── repository/     → Acesso a dados
└── dto/                → Objetos de entrada/saída (records), isolando a API do modelo de domínio
```

---

## ☁️ Deploy e Infraestrutura

A aplicação está containerizada com **Docker** e implantada na **AWS**, com banco de dados desacoplado da aplicação — uma prática essencial para que os dados sobrevivam independentemente do ciclo de vida da instância que roda o código.

- **Imagem Docker** — build multi-stage: um estágio compila o projeto com Maven, o outro roda apenas o `.jar` final sobre uma imagem JRE enxuta (`eclipse-temurin:17-jre-alpine`), executando com um usuário não-root por segurança.
- **AWS Elastic Beanstalk** — orquestra o provisionamento e a execução do container em produção, com variáveis de ambiente (credenciais de banco, e-mail, JWT) configuradas fora do código-fonte.
- **AWS RDS (MySQL)** — banco de dados gerenciado, separado da aplicação, sem acesso público direto (`Publicly accessible: No`), acessível apenas pelo Security Group do ambiente da aplicação.

Essa separação entre aplicação (efêmera, pode ser recriada a qualquer momento pelo Beanstalk) e banco de dados (persistente, no RDS) evita perda de dados em caso de reinicialização ou substituição da instância da aplicação.

---

## 📖 Documentação da API

Com a aplicação em execução localmente, a documentação interativa fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

A aplicação também está em execução na AWS, com a documentação acessível publicamente em:

```
http://wellrx-env.eba-hucxp6zp.us-east-2.elasticbeanstalk.com/swagger-ui/index.html
```

---

## 🗺️ Roadmap

- [ ] Log de auditoria de acesso a dados clínicos (quem visualizou o prontuário de quem, e quando)
- [ ] Testes automatizados com JUnit e Mockito
- [ ] Anonimização de dados para conformidade com o direito ao esquecimento (LGPD)
- [ ] Geração de receituário em PDF para impressão/entrega ao paciente
- [ ] HTTPS via Load Balancer com certificado gerenciado (AWS ACM)
- [ ] CI/CD para deploy automático a cada push na branch principal

---

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir uma *issue* ou enviar um *pull request* com sugestões, melhorias ou correções.
