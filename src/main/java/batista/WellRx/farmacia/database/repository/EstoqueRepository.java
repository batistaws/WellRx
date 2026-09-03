package batista.WellRx.farmacia.database.repository;

import batista.WellRx.farmacia.database.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository <Estoque, Long>{

    Optional<Estoque> findByMedicamentoId(Long medicamentoId);


}
