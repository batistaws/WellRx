package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Especialidade;
import batista.WellRx.clinica.database.model.Medico;
public record ListarMedicoDto(

        Long id,
        String nomeCompleto,
        String cpf,
        String crm,
        Especialidade especialidade,
        String telefone,
        Boolean ativo
) {

    public ListarMedicoDto(Medico medico) {
        this(
                medico.getId(),
                medico.getNomeCompleto(),
                medico.getUsuario().getCpf(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getTelefone(),
                medico.getUsuario().getAtivo()
        );
    }
}
