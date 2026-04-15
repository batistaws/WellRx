package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Endereco;
import batista.WellRx.clinica.database.model.Sexo;

public record ListagemPacienteDto(

        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        String dataNascimento,
        CadastroEnderecoDto endereco,
        Sexo sexo
) {
}
