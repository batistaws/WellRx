package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Paciente;
import batista.WellRx.clinica.database.model.Recepcionista;

public record ListarPacienteDto(

        Long id,
        String nomeCompleto,
        String cpf,
        String telefone,
        Boolean ativo
) {

    public ListarPacienteDto(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNomeCompleto(),
                paciente.getTelefone(),
                paciente.getUsuario().getCpf(),
                paciente.getUsuario().getAtivo()
        );
    }

}
