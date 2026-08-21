package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Atendimento;

import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoResumoDto(


        Long id,
        LocalDateTime dataAtendimento,
        String queixa,
        String diagnostico,
        String condutas,
        List<PrescricaoResumoDto> prescricoes,
        List<ExameResumoDto> exames
) {

    public AtendimentoResumoDto(Atendimento atendimento) {
        this(atendimento.getId(),
                atendimento.getDataAtendimento(),
                atendimento.getQueixa(),
                atendimento.getDiagnostico(),
                atendimento.getCondutas(),
                atendimento.getPrescricoes().stream().map(PrescricaoResumoDto::new).toList(),
                atendimento.getExames().stream().map(ExameResumoDto::new).toList());
    }
}
