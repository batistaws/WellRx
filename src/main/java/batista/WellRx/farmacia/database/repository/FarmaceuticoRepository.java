package batista.WellRx.farmacia.database.repository;

import batista.WellRx.farmacia.database.model.Farmaceutico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmaceuticoRepository extends JpaRepository<Farmaceutico, Long> {
    Optional<Farmaceutico> findByUsuarioId(Long id);
}
