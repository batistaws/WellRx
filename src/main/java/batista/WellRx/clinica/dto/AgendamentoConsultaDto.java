package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Especialidade;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoConsultaDto (
        Long idMedico,

        @NotNull
        Long idPaciente,

        @NotNull
        @Future
        LocalDateTime data,

        String motivoConsulta,

        Especialidade especialidade
){
}
