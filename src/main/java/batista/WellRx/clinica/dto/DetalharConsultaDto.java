package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Consulta;
import batista.WellRx.clinica.database.model.MotivoCancelamento;
import batista.WellRx.clinica.database.model.StatusConsulta;

import java.time.LocalDateTime;

public record DetalharConsultaDto(


        Long id,
        Long idMedico,
        String nomeMedico,
        Long idPaciente,
        String nomePaciente,
        LocalDateTime data,
        StatusConsulta status,
        String motivoConsulta,
        MotivoCancelamento motivoCancelamento) {

    public DetalharConsultaDto(Consulta consulta) {
        this(consulta.getId(),
             consulta.getMedico().getId(),
             consulta.getMedico().getNomeCompleto(),
             consulta.getPaciente().getId(),
             consulta.getPaciente().getNomeCompleto(),
             consulta.getData(),
             consulta.getStatusConsulta(),
             consulta.getMotivoConsulta(),
             consulta.getMotivoCancelamento());
    }
}


