package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Comorbidade;

import java.time.LocalDate;

public record ComorbidadeDto(
        Long id,
        String condicao,
        LocalDate dataDiagnostico) {

    public ComorbidadeDto(Comorbidade comorbidade) {
        this(comorbidade.getId(), comorbidade.getCondicao(), comorbidade.getDataDiagnostico());
    }
}
