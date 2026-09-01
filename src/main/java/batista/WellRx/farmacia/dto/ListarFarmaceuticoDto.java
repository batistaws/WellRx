package batista.WellRx.farmacia.dto;

import batista.WellRx.farmacia.database.model.Farmaceutico;

public record ListarFarmaceuticoDto(



        Long id,
        String nomeCompleto,
        String cpf,
        String telefone,
        Boolean ativo
) {

    public ListarFarmaceuticoDto(Farmaceutico farmaceutico) {
        this(
                farmaceutico.getId(),
                farmaceutico.getNomeCompleto(),
                farmaceutico.getUsuario().getCpf(),
                farmaceutico.getTelefone(),
                farmaceutico.getUsuario().getAtivo()
        );
    }
}

