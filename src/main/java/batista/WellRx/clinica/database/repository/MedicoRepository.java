package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Especialidade;
import batista.WellRx.clinica.database.model.Medico;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;


public interface MedicoRepository extends JpaRepository<Medico, Long>, JpaSpecificationExecutor<Medico> {
    Optional<Medico> findByCrm(String crm);


    @Query("""
        SELECT m FROM Medico m
        WHERE
        m.usuario.ativo = true
        AND
        m.especialidade = :especialidade
        AND
        m.id not in(
            SELECT c.medico.id
            FROM Consulta c
            WHERE
            c.data = :data
        )
        ORDER BY rand()
        LIMIT 1
        """)
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, @NotNull @Future LocalDateTime data);

    @Query("""
            SELECT u.ativo\s
             FROM Medico m\s
             JOIN m.usuario u\s
             WHERE m.id = :id
            """)
    boolean findAtivoById(Long id);
}
