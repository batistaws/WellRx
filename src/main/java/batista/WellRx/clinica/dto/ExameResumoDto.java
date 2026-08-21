package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Exame;

public record ExameResumoDto(
        Long id,
        String tipoExame,
        String status) {

    public ExameResumoDto(Exame exame) {
        this(exame.getId(), exame.getTipo(), exame.getStatus().name());
    }
}
