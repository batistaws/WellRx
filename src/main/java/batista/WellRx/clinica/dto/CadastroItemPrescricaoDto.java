package batista.WellRx.clinica.dto;

import batista.WellRx.farmacia.database.model.Medicamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CadastroItemPrescricaoDto(

        @NotNull
        Long medicamentoId,

        @NotBlank
        String dosagem,

        @NotBlank
        String posologia,

        @NotNull
        @Positive
        Integer duracaoDias
) {
}
