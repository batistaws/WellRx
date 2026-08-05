package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Prescricao;

import java.time.LocalDateTime;

public record ListarPrescricaoDto(

        Long id,
        Long idPaciente,
        String nomePaciente,
        Long idMedico,
        String nomeMedico,
        LocalDateTime dataEmissao) {

    public ListarPrescricaoDto(Prescricao prescricao) {
        this(prescricao.getId(),
                prescricao.getAtendimento().getProntuario().getPaciente().getId(),
                prescricao.getAtendimento().getProntuario().getPaciente().getNomeCompleto(),
                prescricao.getAtendimento().getMedico().getId(),
                prescricao.getAtendimento().getMedico().getNomeCompleto(),
                prescricao.getDataEmissao());
    }
}
