package batista.WellRx.clinica.database.model;

import batista.WellRx.clinica.dto.CadastroPacienteDto;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {


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

    public Paciente(@Valid CadastroPacienteDto dto, Usuario usuario) {
        this.nomeCompleto = dto.nomeCompleto();
        this.cpf = dto.cpf();
        this.telefone = dto.telefone();
        this.dataNascimento = dto.dataNascimento();
        this.sexo = dto.sexo();
        this.endereco = new Endereco(dto.endereco());
        this.usuario = usuario;
    }
}
