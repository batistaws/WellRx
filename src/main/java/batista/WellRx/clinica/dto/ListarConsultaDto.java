package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Consulta;

import java.time.LocalDateTime;

public record ListarConsultaDto(

        Long id,
        Long idMedico,
        Long idPaciente,
        LocalDateTime data) {


    public ListarConsultaDto(Consulta consulta) {
        this(consulta.getId(),
             consulta.getMedico().getId(),
             consulta.getPaciente().getId(),
             consulta.getData());
    }
}
