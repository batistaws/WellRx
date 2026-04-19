package batista.WellRx.clinica.database.model;

import batista.WellRx.clinica.dto.CadastroMedicoDto;
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
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String crm;
    private String cpf;
    private String especialidade;
    private String telefone;
    private LocalDate dataNascimento;


    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Embedded
    private Endereco endereco;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public Medico(CadastroMedicoDto dto, Usuario usuario) {
        this.nomeCompleto = dto.nomeCompleto();
        this.crm = dto.crm();
        this.cpf = dto.cpf();
        this.especialidade = dto.especialidade();
        this.especialidade = dto.telefone();
        this.dataNascimento = dto.dataNascimento();
        this.telefone = dto.telefone();
        this.sexo = dto.sexo();
        this.endereco = new Endereco(dto.endereco());
        this.usuario = usuario;
    }
    
}
