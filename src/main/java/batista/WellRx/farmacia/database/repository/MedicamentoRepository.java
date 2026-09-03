package batista.WellRx.farmacia.database.repository;

import batista.WellRx.farmacia.database.model.Medicamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository <Medicamento, Long> {

    Page<Medicamento> findByNomeContainingIgnoreCase(String nome, Pageable paginacao);

}
