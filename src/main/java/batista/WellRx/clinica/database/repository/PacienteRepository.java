package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Paciente;
import batista.WellRx.clinica.database.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PacienteRepository extends JpaRepository <Paciente, Long>, JpaSpecificationExecutor<Paciente> {


}
