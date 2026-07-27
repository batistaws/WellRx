package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Atendimento;

import java.time.LocalDateTime;

public record ListarAtendimentosDto(

        Long id,
        Long idPaciente,
        String nomePaciente,
        Long idMedico,
        String nomeMedico,
        LocalDateTime data,
        String diagnostico) {

    public  ListarAtendimentosDto(Atendimento atendimento) {
        this(atendimento.getId(),
                atendimento.getConsulta().getPaciente().getId(),
                atendimento.getConsulta().getPaciente().getNomeCompleto(),
                atendimento.getConsulta().getMedico().getId(),
                atendimento.getConsulta().getMedico().getNomeCompleto(),
                atendimento.getDataAtendimento(),
                atendimento.getDiagnostico());
    }
}
