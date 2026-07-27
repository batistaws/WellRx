package batista.WellRx.clinica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroAtendimentoDto(


        @NotBlank
        String queixa,

        @NotBlank
        String diagnostico,

        @NotBlank
        String condutas,

        @NotNull
        @Valid
        CadastroSinaisVitaisDto sinaisVitais

) {
}
