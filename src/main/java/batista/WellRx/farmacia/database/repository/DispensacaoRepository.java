package batista.WellRx.farmacia.database.repository;

import batista.WellRx.farmacia.database.model.Dispensacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispensacaoRepository extends JpaRepository<Dispensacao, Long> {

    Optional<Dispensacao> findByItemPrescricaoId(Long itemPrescricaoId);

}
