package batista.WellRx.infra.exeption.validacoes;

import batista.WellRx.clinica.dto.AgendamentoConsultaDto;

public interface ValidadorAgendamentoConsulta {

    void validar(AgendamentoConsultaDto dados);
}
