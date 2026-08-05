package batista.WellRx.clinica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CadastroPrescricaoDto (

        String observacao,

        @NotEmpty
        @Valid
        List<CadastroItemPrescricaoDto> itens
){
}
