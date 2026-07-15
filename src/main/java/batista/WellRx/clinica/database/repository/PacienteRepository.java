package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Paciente;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository <Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    @Query("""
    SELECT u.ativo 
    FROM Paciente p 
    JOIN p.usuario u 
    WHERE p.id = :id
""")
    Boolean findAtivoById(@NotNull Long id);
}
