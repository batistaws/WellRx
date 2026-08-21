package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Prontuario;

import java.time.LocalDate;
import java.util.List;

public record DetalharProntuarioDto(

        Long id,
        Long idPaciente,
        String nomePaciente,
        LocalDate dataCriacao,
        List<AlergiaDto> alergias,
        List<ComorbidadeDto> comorbidades,
        List<AtendimentoResumoDto> atendimentos) {

    public DetalharProntuarioDto(Prontuario prontuario) {
        this(prontuario.getId(),
                prontuario.getPaciente().getId(),
                prontuario.getPaciente().getNomeCompleto(),
                prontuario.getDataCriacao(),
                prontuario.getAlergias().stream().map(AlergiaDto::new).toList(),
                prontuario.getComorbidades().stream().map(ComorbidadeDto::new).toList(),
                prontuario.getAtendimentos().stream().map(AtendimentoResumoDto::new).toList());
    }
}
