package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Exame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExameRepository extends JpaRepository<Exame, Long> {
    Page<Exame> findByAtendimento_Medico_Id(Long id, Pageable paginacao);

    Page<Exame> findByAtendimento_Prontuario_Paciente_Id(Long id, Pageable paginacao);
}
