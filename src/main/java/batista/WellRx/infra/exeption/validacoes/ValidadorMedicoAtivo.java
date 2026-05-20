package batista.WellRx.infra.exeption.validacoes;


import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.dto.AgendamentoConsultaDto;

import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoConsulta {

private final MedicoRepository repository;

    public ValidadorMedicoAtivo(MedicoRepository repository) {
        this.repository = repository;
    }

    public void validar(AgendamentoConsultaDto dto ){

        if (dto.idMedico() == null){
            return;
        }

        var medicoAtivo = repository.findAtivoById(dto.idMedico());
        if (!medicoAtivo){
            throw new RegraNegocioException("Consulta não pode ser agendada com médico excluido");
        }
    }
}
