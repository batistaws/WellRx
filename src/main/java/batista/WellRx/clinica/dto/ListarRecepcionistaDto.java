package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Recepcionista;

public record ListarRecepcionistaDto(

        Long id,
        String nomeCompleto,
        String cpf,
        String telefone,
        String ramal,
        Boolean ativo
) {
    public ListarRecepcionistaDto(Recepcionista recepcionista) {
        this(
                recepcionista.getId(),
                recepcionista.getNomeCompleto(),
                recepcionista.getTelefone(),
                recepcionista.getUsuario().getCpf(),
                recepcionista.getRamal(),
                recepcionista.getUsuario().getAtivo()
        );
    }
}
