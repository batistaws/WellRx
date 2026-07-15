package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Consulta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;


public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndData(Long id, @NotNull @Future LocalDateTime data);

    boolean existsByPacienteIdAndDataBetween(@NotNull Long id, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.usuario.id = :id")
    Page<Consulta> findAllByPacienteUsuarioId(@Param("id") Long id, Pageable paginacao);



}
