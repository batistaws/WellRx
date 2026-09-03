package batista.WellRx.farmacia.controller;

import batista.WellRx.farmacia.database.model.Dispensacao;

import java.time.LocalDateTime;

public record ListagemDispensacaoDto(

        Long id,
        Long idItemPrescricao,
        String medicamento,
        Long idFarmaceutico,
        String nomeFarmaceutico,
        Integer quantidadeDispensada,
        LocalDateTime dataDispensacao) {

    public ListagemDispensacaoDto(Dispensacao dispensacao) {
        this(dispensacao.getId(),
                dispensacao.getItemPrescricao().getId(),
                dispensacao.getItemPrescricao().getMedicamento().getNome(),
                dispensacao.getFarmaceutico().getId(),
                dispensacao.getFarmaceutico().getNomeCompleto(),
                dispensacao.getQuantidadeDispensada(),
                dispensacao.getDataDispensacao());
    }
}
