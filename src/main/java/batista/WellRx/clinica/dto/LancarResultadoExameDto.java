package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.NotBlank;

public record LancarResultadoExameDto(

        @NotBlank
        String laudo
) {
}
