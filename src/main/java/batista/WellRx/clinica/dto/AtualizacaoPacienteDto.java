package batista.WellRx.clinica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoPacienteDto(

        @NotNull
        Long id,

        String telefone,

        @NotNull
        @Valid
        CadastroEnderecoDto endereco
) {
}
