package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.ItemPrescricao;

public record ItemPrescricaoDto(

        String medicamento,
        String dosagem,
        String posologia,
        Integer duracaoDias) {

    public ItemPrescricaoDto(ItemPrescricao item) {
        this(item.getMedicamento(), item.getDosagem(), item.getPosologia(), item.getDuracaoDias());
    }
}
