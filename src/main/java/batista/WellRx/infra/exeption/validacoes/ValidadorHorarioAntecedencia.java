package batista.WellRx.infra.exeption.validacoes;

import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoConsulta {

    public void validar(AgendamentoConsultaDto dados) {
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new RegraNegocioException("Consulta deve ser agendada com antecedência miníma de 30 minutos");
        }
    }
}
