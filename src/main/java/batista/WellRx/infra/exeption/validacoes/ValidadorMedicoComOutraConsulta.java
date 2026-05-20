package batista.WellRx.infra.exeption.validacoes;


import batista.WellRx.clinica.controller.ConsultaRepository;
import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoComOutraConsulta implements ValidadorAgendamentoConsulta {

    private final ConsultaRepository repository;

    public ValidadorMedicoComOutraConsulta(ConsultaRepository repository) {
        this.repository = repository;
    }

    public void validar(AgendamentoConsultaDto dados){
        var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
        if (medicoPossuiOutraConsultaNoMesmoHorario){
            throw new RegraNegocioException( "Medico ja possui outra consulta agenda neste dia");
        }
    }
}
