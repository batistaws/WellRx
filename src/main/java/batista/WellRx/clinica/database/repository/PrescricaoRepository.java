package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Prescricao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

    Page<Prescricao> findByAtendimento_Medico_Id(Long id, Pageable paginacao);

    Page<Prescricao> findByAtendimento_Prontuario_Paciente_Id(Long id, Pageable paginacao);
}
