package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CadastroItemPrescricaoDto(

        @NotBlank
        String medicamento,

        @NotBlank
        String dosagem,

        @NotBlank
        String posologia,

        @NotNull
        @Positive
        Integer duracaoDias
) {
}
