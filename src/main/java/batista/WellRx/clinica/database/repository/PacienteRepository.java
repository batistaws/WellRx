package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Paciente;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PacienteRepository extends JpaRepository <Paciente, Long>, JpaSpecificationExecutor<Paciente> {


    Boolean findAtivoById(@NotNull Long id);
}
