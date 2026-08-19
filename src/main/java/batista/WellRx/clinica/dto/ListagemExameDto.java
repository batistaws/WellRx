package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Exame;

import java.time.LocalDateTime;

public record ListagemExameDto(

        Long id,
        Long idAtendimento,
        Long idMedico,
        String nomeMedico,
        Long idPaciente,
        String nomePaciente,
        String tipoExame,
        String status,
        LocalDateTime dataAgendamento,
        String laudo,
        LocalDateTime dataResultado) {

    public ListagemExameDto(Exame exame) {
        this(exame.getId(),
                exame.getAtendimento().getId(),
                exame.getAtendimento().getMedico().getId(),
                exame.getAtendimento().getMedico().getNomeCompleto(),
                exame.getAtendimento().getProntuario().getPaciente().getId(),
                exame.getAtendimento().getProntuario().getPaciente().getNomeCompleto(),
                exame.getTipo(),
                exame.getStatus().name(),
                exame.getDataAgendamento(),
                exame.getResultadoExame() != null ? exame.getResultadoExame().getLaudo() : null,
                exame.getResultadoExame() != null ? exame.getResultadoExame().getDataResultado() : null);
    }
}
