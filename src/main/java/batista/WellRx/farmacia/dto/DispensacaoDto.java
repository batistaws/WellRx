package batista.WellRx.farmacia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DispensacaoDto(

        @NotNull
        @Positive
        Integer quantidadeDispensada) {
}
