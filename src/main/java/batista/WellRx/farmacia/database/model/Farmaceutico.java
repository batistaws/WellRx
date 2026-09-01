package batista.WellRx.farmacia.database.model;

import batista.WellRx.clinica.database.model.Endereco;
import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.model.Sexo;
import batista.WellRx.clinica.dto.AtualizacaoMedicoDto;
import batista.WellRx.farmacia.dto.AtualizacaoFarmaceuticoDto;
import batista.WellRx.farmacia.dto.CadastroFarmaceuticoDto;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "farmaceuticos")
public class Farmaceutico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Embedded
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Farmaceutico(CadastroFarmaceuticoDto dto, Usuario usuario) {
        this.nomeCompleto = dto.nomeCompleto();
        this.cpf = dto.cpf();
        this.telefone = dto.telefone();
        this.dataNascimento = dto.dataNascimento();
        this.sexo = dto.sexo();
        this.endereco = new Endereco(dto.endereco());
        this.usuario = usuario;
    }
    public Farmaceutico atualizarInformacoes(AtualizacaoFarmaceuticoDto dto) {
        if (dto.telefone() != null) {
            this.telefone = dto.telefone();
        }
        if (dto.endereco() != null) {
            this.endereco.atualizarEndereco(dto.endereco());
        }
        return this;
    }
}
