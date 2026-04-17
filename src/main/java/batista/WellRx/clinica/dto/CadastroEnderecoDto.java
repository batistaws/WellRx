package batista.WellRx.clinica.dto;

import batista.WellRx.clinica.database.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CadastroEnderecoDto(

        @NotBlank String logradouro,
        @NotBlank String bairro,
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,
        @NotBlank String cidade,
        @NotBlank String estado,
        String numero,
        String complemento
) {
    public CadastroEnderecoDto(Endereco endereco) {
        this(endereco.getLogradouro(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(), endereco.getEstado(), endereco.getNumero(), endereco.getComplemento());
    }
}

