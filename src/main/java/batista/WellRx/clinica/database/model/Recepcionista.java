package batista.WellRx.clinica.database.model;

import batista.WellRx.clinica.dto.CadastroPacienteDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recepcionistas")
public class Recepcionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;
    private String ramal;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Recepcionista(@Valid CadastroRecepcionistaDto dto, Usuario usuario) {
        this.nomeCompleto = dto.nomeCompleto();
        this.cpf = dto.cpf();
        this.telefone = dto.telefone();
        this.dataNascimento = dto.dataNascimento();
        this.ramal = dto.ramal();
        this.sexo = dto.sexo();
        this.endereco = new Endereco(dto.endereco());
        this.turno = dto.turno();
        this.usuario = usuario;
    }
}
