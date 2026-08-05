package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {



    Optional<Prontuario> findByPacienteId(Long paciente);
}
