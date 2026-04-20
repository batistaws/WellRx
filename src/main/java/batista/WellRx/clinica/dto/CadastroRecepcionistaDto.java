package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Sexo;
import batista.WellRx.clinica.database.model.Turno;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CadastroRecepcionistaDto(


        @NotBlank
        String nomeCompleto,

        @NotBlank
        String email,

        @NotBlank @Size(min = 8 , message = "A senha deve conter no mínimo 8 caracteres")
        String senha,

        @NotBlank
        String confirmacaoSenha,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}" , message = "O telefone deve conter apenas números e 11 dígitos, formato DDD999999999")
        String telefone,

        @NotBlank
        @Size(min = 14, max = 14, message = "O CPF deve ter exatamente 14 caracteres (formato 000.000.000-00)")
        String cpf,

        @NotBlank
        @Pattern(regexp = "\\d{4}-?\\d{4}", message = "Ramal deve conter 8 dígitos (ex: 12345678 ou 1234-5678)")
        String ramal,

        @NotNull
        Turno turno,

        @NotNull
        LocalDate dataNascimento,

        @NotNull
        @Valid
        CadastroEnderecoDto endereco,

        @NotNull
        Sexo sexo
) {
}
