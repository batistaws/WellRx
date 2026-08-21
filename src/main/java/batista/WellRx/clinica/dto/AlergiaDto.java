package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Alergia;

public record AlergiaDto(
        Long id,
        String substancia,
        String gravidade) {

    public AlergiaDto(Alergia alergia) {
        this(alergia.getId(), alergia.getSubstancia(), alergia.getGravidade().name());
    }
}
