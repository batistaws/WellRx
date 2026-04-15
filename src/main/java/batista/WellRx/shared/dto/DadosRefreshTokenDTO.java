package batista.WellRx.shared.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshTokenDTO (
        @NotBlank
        String refreshToken
) {
}
