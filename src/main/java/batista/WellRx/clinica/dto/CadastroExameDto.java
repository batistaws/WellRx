package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CadastroExameDto(

        @NotBlank
        String tipoExame,

        @NotNull
        @Future
        LocalDateTime dataAgendada) {

}
