package batista.WellRx.farmacia.service;

import batista.WellRx.farmacia.database.model.Medicamento;
import batista.WellRx.farmacia.database.repository.MedicamentoRepository;
import batista.WellRx.farmacia.dto.CadastroMedicamentoDto;
import batista.WellRx.farmacia.dto.ListagemMedicamentoDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repository;

    public MedicamentoService(MedicamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ListagemMedicamentoDto cadastrar(CadastroMedicamentoDto dto) {

        var medicamento = new Medicamento(dto.nome(), dto.principioAtivo(), dto.controlado(), dto.unidadeMedida());
        repository.save(medicamento);
        return new ListagemMedicamentoDto(medicamento);
    }

    public ListagemMedicamentoDto detalhar(Long id) {
        var medicamento = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Medicamento não encontrado"));
        return new ListagemMedicamentoDto(medicamento);
    }

    public Page<ListagemMedicamentoDto> listar(String nome, Pageable paginacao) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, paginacao).map(ListagemMedicamentoDto::new);
        }
        return repository.findAll(paginacao).map(ListagemMedicamentoDto::new);
    }
}
