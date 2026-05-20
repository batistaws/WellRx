package batista.WellRx.infra.exeption.validacoes;

import batista.WellRx.clinica.database.repository.PacienteRepository;

import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoConsulta {

    private final PacienteRepository repository;

    public ValidadorPacienteAtivo(PacienteRepository repository) {
        this.repository = repository;
    }

    public  void validar(AgendamentoConsultaDto dto){
        var pacienteAtivo = repository.findAtivoById(dto.idPaciente());
        if (!pacienteAtivo){
            throw new RegraNegocioException("Consulta não pode ser agendada com paciente excluido");
        }
    }
}
