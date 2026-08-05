package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Prescricao;

import java.util.List;

public record ListagemPrescricaoDto(

        Long id,
        Long idAtendimento,
        Long idMedico,
        String nomeMedico,
        Long idPaciente,
        String nomePaciente,
        String observacao,
        List<ItemPrescricaoDto> itens
) {

    public ListagemPrescricaoDto(Prescricao prescricao){
        this(prescricao.getId(),
                prescricao.getAtendimento().getId(),
                prescricao.getAtendimento().getConsulta().getMedico().getId(),
                prescricao.getAtendimento().getConsulta().getMedico().getNomeCompleto(),
                prescricao.getAtendimento().getConsulta().getPaciente().getId(),
                prescricao.getAtendimento().getConsulta().getPaciente().getNomeCompleto(),
                prescricao.getObservacoes(),
                prescricao.getItemPrescricaos().stream().map(ItemPrescricaoDto:: new).toList());
    }


}
