package batista.WellRx.farmacia.service;

import batista.WellRx.clinica.database.repository.ItemPrescricaoRepository;
import batista.WellRx.farmacia.dto.DispensacaoDto;
import batista.WellRx.farmacia.dto.ListagemDispensacaoDto;
import batista.WellRx.farmacia.database.model.Dispensacao;
import batista.WellRx.farmacia.database.repository.DispensacaoRepository;
import batista.WellRx.farmacia.database.repository.EstoqueRepository;
import batista.WellRx.farmacia.database.repository.FarmaceuticoRepository;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DispensacaoService {


    private final DispensacaoRepository dispensacaoRepository;
    private final ItemPrescricaoRepository itemPrescricaoRepository;
    private final FarmaceuticoRepository farmaceuticoRepository;
    private final EstoqueRepository estoqueRepository;

    public DispensacaoService(DispensacaoRepository dispensacaoRepository,
                              ItemPrescricaoRepository itemPrescricaoRepository,
                              FarmaceuticoRepository farmaceuticoRepository,
                              EstoqueRepository estoqueRepository) {
        this.dispensacaoRepository = dispensacaoRepository;
        this.itemPrescricaoRepository = itemPrescricaoRepository;
        this.farmaceuticoRepository = farmaceuticoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public ListagemDispensacaoDto dispensar(Long itemPrescricaoId, DispensacaoDto dto, Usuario logado) {

        var farmaceutico = farmaceuticoRepository.findByUsuarioId(logado.getId())
                .orElseThrow(() -> new RegraNegocioException("Farmaceutico não existe."));

        var itemPrescricao = itemPrescricaoRepository.findById(itemPrescricaoId)
                .orElseThrow(() -> new RegraNegocioException("Item de prescrição não encontrado"));

        if (dispensacaoRepository.findByItemPrescricaoId(itemPrescricaoId).isPresent()) {
            throw new RegraNegocioException("Este item já foi dispensado anteriormente.");
        }

        var estoque = estoqueRepository.findByMedicamentoId(itemPrescricao.getMedicamento().getId())
                .orElseThrow(() -> new RegraNegocioException("Não há estoque cadastrado para este medicamento."));

        estoque.remover(dto.quantidadeDispensada());

        var dispensacao = new Dispensacao(itemPrescricao, farmaceutico, dto.quantidadeDispensada());
        dispensacaoRepository.save(dispensacao);

        return new ListagemDispensacaoDto(dispensacao);
    }

    public ListagemDispensacaoDto detalhar(Long id) {
        var dispensacao = dispensacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Dispensação não encontrada"));
        return new ListagemDispensacaoDto(dispensacao);
    }
}
