package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.GravidadeAlergia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroAlergiaDto(

        @NotBlank
        String substancia,

        @NotNull
        GravidadeAlergia gravidade) {
}
