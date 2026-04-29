package batista.WellRx.clinica.database.model;


import batista.WellRx.clinica.dto.CadastroEnderecoDto;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String estado;
        private String cep;

    public Endereco(@NotBlank CadastroEnderecoDto endereco) {
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.estado = endereco.estado();
        this.cep = endereco.cep();
    }

    public Endereco atualizarEndereco(@NotNull @Valid CadastroEnderecoDto dto) {
        if (dto.logradouro() != null) {
            this.logradouro = dto.logradouro();
        }
        if (dto.numero() != null) {
            this.numero = dto.numero();
        }
        if (dto.complemento() != null) {
            this.complemento = dto.complemento();
        }
        if (dto.bairro() != null) {
            this.bairro = dto.bairro();
        }
        if (dto.cidade() != null) {
            this.cidade = dto.cidade();
        }
        if (dto.estado() != null) {
            this.estado = dto.estado();
        }
        if (dto.cep() != null) {
            this.cep = dto.cep();
        }
        return this;
    }
}
