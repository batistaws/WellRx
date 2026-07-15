package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record CancelamentoConsultaDto(

        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo
) {
}
