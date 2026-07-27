package batista.WellRx.clinica.database.repository;

import batista.WellRx.clinica.database.model.Atendimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    Page<Atendimento> findByMedicoId(Long id, Pageable paginacao);

    Page<Atendimento> findByProntuario_Paciente_Id(Long pacienteId, Pageable paginacao);

}
