package batista.WellRx.farmacia.database.repository;

import batista.WellRx.farmacia.database.model.Farmaceutico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmaceuticoRepository extends JpaRepository<Farmaceutico, Long> {
}
