package batista.WellRx.farmacia.dto;

import batista.WellRx.farmacia.database.model.Estoque;

public record ListagemEstoqueDto(

        Long id,
        Long idMedicamento,
        String nomeMedicamento,
        Integer quantidadeDisponivel) {

    public ListagemEstoqueDto(Estoque estoque) {
        this(estoque.getId(),
                estoque.getMedicamento().getId(),
                estoque.getMedicamento().getNome(),
                estoque.getQuantidadeDisponivel());
    }
}
