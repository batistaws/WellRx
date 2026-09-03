package batista.WellRx.farmacia.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DispensacaoDto(

        @NotNull
        @Positive
        Integer quantidadeDispensada) {
}
