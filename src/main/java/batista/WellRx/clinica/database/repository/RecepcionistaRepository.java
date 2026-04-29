package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long>, JpaSpecificationExecutor<Recepcionista> {


}
