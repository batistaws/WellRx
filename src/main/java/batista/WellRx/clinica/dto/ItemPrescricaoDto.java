package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.ItemPrescricao;

public record ItemPrescricaoDto(

        Long idMedicamento,
        String nomeMedicamento,
        String dosagem,
        String posologia,
        Integer duracaoDias) {

    public ItemPrescricaoDto(ItemPrescricao item) {
        this(item.getMedicamento().getId(),
                item.getMedicamento().getNome(),
                item.getDosagem(),
                item.getPosologia(),
                item.getDuracaoDias());
    }
}
