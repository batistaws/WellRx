package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
}
