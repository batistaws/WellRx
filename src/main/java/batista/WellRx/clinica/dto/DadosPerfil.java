package batista.WellRx.clinica.dto;

import batista.WellRx.shared.database.model.PerfilEnum;
import jakarta.validation.constraints.NotNull;

public record DadosPerfil(

        @NotNull PerfilEnum perfilNome) {

}
