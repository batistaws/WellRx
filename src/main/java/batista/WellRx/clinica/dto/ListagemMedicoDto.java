package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Especialidade;
import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.model.Sexo;

import java.time.LocalDate;

public record ListagemMedicoDto(

        Long id,
        String nomeCompleto,
        String email,
        String crm,
        String cpf,
        Especialidade especialidade,
        String telefone,
        LocalDate dataNascimento,
        CadastroEnderecoDto endereco,
        Sexo sexo
) {
    public ListagemMedicoDto(Medico medico) {
        this(
                medico.getId(),
                medico.getNomeCompleto(),
                medico.getUsuario().getEmail(),
                medico.getCrm(),
                medico.getCpf(),
                medico.getEspecialidade(),
                medico.getTelefone(),
                medico.getDataNascimento(),
                new CadastroEnderecoDto(medico.getEndereco()), medico.getSexo());
    }
}
