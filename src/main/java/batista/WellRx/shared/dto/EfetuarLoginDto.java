package batista.WellRx.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EfetuarLoginDto (

        @NotBlank
        @Size(min = 14, max = 14, message = "O CPF deve ter exatamente 14 caracteres (formato 000.000.000-00)")
        String cpf,
        @NotBlank String senha
) {
}
