package batista.WellRx.farmacia.dto;

import batista.WellRx.clinica.dto.CadastroEnderecoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoFarmaceuticoDto (

    @NotNull
    Long id,

    String telefone,

    @NotNull
    @Valid
    CadastroEnderecoDto endereco
) {

}

