package batista.WellRx.farmacia.dto;

import batista.WellRx.farmacia.database.model.Medicamento;

public record ListagemMedicamentoDto(

        Long id,
        String nome,
        String principioAtivo,
        Boolean controlado,
        String unidadeMedida) {

    public ListagemMedicamentoDto(Medicamento medicamento) {
        this(medicamento.getId(),
                medicamento.getNome(),
                medicamento.getPrincipioAtivo(),
                medicamento.getControlado(),
                medicamento.getUnidadeMedida());
    }
}
