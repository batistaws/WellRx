package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.model.Recepcionista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface MedicoRepository extends JpaRepository<Medico, Long>, JpaSpecificationExecutor<Medico> {
    Optional<Medico> findByCrm(String crm);


}
