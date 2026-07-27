package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Atendimento;

import java.time.LocalDateTime;

public record ListagemAtendimentoDto(

        Long id,
        Long idConsulta,
        Long idMedico,
        String nomeMedico,
        Long idPaciente,
        String nomePaciente,
        LocalDateTime dataAtendimento,
        String queixa,
        String diagnostico,
        String condutas,
        DadosSinaisVitaisDto sinaisVitais) {

    public ListagemAtendimentoDto(Atendimento atendimento) {
        this(atendimento.getId(),
                atendimento.getConsulta().getId(),
                atendimento.getMedico().getId(),
                atendimento.getMedico().getNomeCompleto(),
                atendimento.getConsulta().getPaciente().getId(),
                atendimento.getConsulta().getPaciente().getNomeCompleto(),
                atendimento.getDataAtendimento(),
                atendimento.getQueixa(),
                atendimento.getDiagnostico(),
                atendimento.getCondutas(),
                atendimento.getSinaisVitais() != null
                ? new DadosSinaisVitaisDto(atendimento.getSinaisVitais())
                : null);
    }
}
