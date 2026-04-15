
package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CadastroPacienteDto(

        @NotBlank
        String nomeCompleto,
        @NotBlank
        String email,
        @NotBlank @Size(min = 8 , message = "A senha deve conter no mínimo 8 caracteres")
        String senha,
        @NotBlank
        String confirmacaoSenha,
        @NotBlank
        String telefone,
        @NotBlank
        @Size(min = 14, max = 14, message = "O CPF deve ter exatamente 14 caracteres (formato 000.000.000-00)")
        String cpf,
        @NotBlank
        LocalDate dataNascimento,
        @NotBlank
        CadastroEnderecoDto endereco,
        @NotBlank
        Sexo sexo


) {
}
