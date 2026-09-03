package batista.WellRx.farmacia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroMedicamentoDto(

        @NotBlank
        String nome,

        String principioAtivo,

        @NotNull
        Boolean controlado,

        @NotBlank
        String unidadeMedida) {
}
