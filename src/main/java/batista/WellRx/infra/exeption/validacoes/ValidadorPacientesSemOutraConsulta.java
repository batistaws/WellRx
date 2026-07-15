package batista.WellRx.infra.exeption.validacoes;

import batista.WellRx.clinica.database.repository.ConsultaRepository;
import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacientesSemOutraConsulta implements ValidadorAgendamentoConsulta {


    private final ConsultaRepository repository;

    public ValidadorPacientesSemOutraConsulta(ConsultaRepository repository) {
        this.repository = repository;
    }

    public  void validar(AgendamentoConsultaDto dto){

        var primeiroHorario = dto.data().withHour(7);
        var ultimoHorario = dto.data().withHour(18);
        var pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dto.idPaciente(), primeiroHorario, ultimoHorario);
        if (pacientePossuiOutraConsultaNoDia){
            throw new RegraNegocioException("Este paciente ja possui outra consulta agendada neste dia");
        }
    }
}
