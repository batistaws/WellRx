package batista.WellRx.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CadastroEnderecoDto(

        @NotBlank String logadouro,
        @NotBlank String bairro,
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,
        @NotBlank String cidade,
        @NotBlank String estado,
        String numero,
        String complemento
) {}

