package batista.WellRx.infra.exeption.validacoes;


import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoConsulta {

    public void validar(AgendamentoConsultaDto dados){
        var dataConsulta = dados.data();

        var dormingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;
        if (dormingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica){
            throw new RegraNegocioException("consulta fora do horario de funcionamento da clinica");
        }

    }
}
