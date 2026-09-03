package batista.WellRx.farmacia.service;

import batista.WellRx.farmacia.database.model.Estoque;
import batista.WellRx.farmacia.database.model.Medicamento;
import batista.WellRx.farmacia.database.repository.EstoqueRepository;
import batista.WellRx.farmacia.database.repository.MedicamentoRepository;
import batista.WellRx.farmacia.dto.ListagemEstoqueDto;
import batista.WellRx.farmacia.dto.ReposicaoEstoqueDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;
    private final MedicamentoRepository medicamentoRepository;

    public EstoqueService(EstoqueRepository repository, MedicamentoRepository medicamentoRepository) {
        this.repository = repository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public ListagemEstoqueDto reporEstoque(Long medicamentoId, ReposicaoEstoqueDto dto, Usuario logado) {

        var medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new RegraNegocioException("Estoque não encontrado para o medicamento ID: " + medicamentoId));

        var estoqueExiste = repository.findByMedicamentoId(medicamentoId);
        if (estoqueExiste.isPresent()) {
            var estoque = estoqueExiste.get();
            estoque.adicionar(dto.quantidade());
            return new ListagemEstoqueDto(estoque);
        }

        var novoEstoque = new Estoque(medicamento, dto.quantidade());
        repository.save(novoEstoque);
        return new ListagemEstoqueDto(novoEstoque);

    }

    public ListagemEstoqueDto detalharPorMedicamento(Long medicamentoId) {

        var estoque = repository.findByMedicamentoId(medicamentoId)
                .orElseThrow(() -> new RegraNegocioException("Estoque não encontrado para este medicamento"));
        return new ListagemEstoqueDto(estoque);
    }

}
