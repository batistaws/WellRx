package batista.WellRx.clinica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoRecepcionistaDto(

        @NotNull
        Long id,

        String telefone,
        String ramal,

        @NotNull
        @Valid
        CadastroEnderecoDto endereco

) {
}
