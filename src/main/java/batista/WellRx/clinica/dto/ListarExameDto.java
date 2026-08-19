package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Exame;

import java.time.LocalDateTime;

public record ListarExameDto(

        Long id,
        String tipoExame,
        String status,
        LocalDateTime dataAgendada) {

    public ListarExameDto(Exame exame) {
        this(exame.getId(),
                exame.getTipo(),
                exame.getStatus().name(),
                exame.getDataAgendamento());
    }
}
