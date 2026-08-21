package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CadastroComorbidadeDto(

        @NotBlank
        String condicao,

        LocalDate dataDiagnostico) {
}
