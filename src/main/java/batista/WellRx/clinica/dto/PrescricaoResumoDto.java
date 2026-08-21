package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Prescricao;

public record PrescricaoResumoDto(
        Long id,
        String observacao) {

    public PrescricaoResumoDto(Prescricao prescricao) {
        this(prescricao.getId(), prescricao.getObservacoes());
    }
}
