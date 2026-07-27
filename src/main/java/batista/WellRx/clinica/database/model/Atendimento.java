package batista.WellRx.clinica.database.model;

import batista.WellRx.clinica.dto.CadastroAtendimentoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "atendimentos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;

    private String queixa;
    private String diagnostico;
    private String condutas;

    @OneToOne(mappedBy = "atendimento", cascade = CascadeType.ALL)
    private SinaisVitais sinaisVitais;

    @OneToMany(mappedBy = "atendimento", cascade = CascadeType.ALL)
    private List<Prescricao> prescricoes = new ArrayList<>();

    @OneToMany(mappedBy = "atendimento", cascade = CascadeType.ALL)
    private List<Exame> exames = new ArrayList<>();


    public Atendimento(CadastroAtendimentoDto dto, Prontuario prontuario, Consulta consulta, Medico medico) {
        this.prontuario = prontuario;
        this.consulta = consulta;
        this.medico = medico;
        this.dataAtendimento = LocalDateTime.now();
        this.queixa = dto.queixa();
        this.diagnostico = dto.diagnostico();
        this.condutas = dto.condutas();

        prontuario.adicionarAtendimento(this);
    }

    public Atendimento atualizar(CadastroAtendimentoDto dto) {
        if (dto.diagnostico() != null) {
            this.diagnostico = dto.diagnostico();
        }
        if (dto.condutas() != null) {
            this.condutas = dto.condutas();
    }
    return this;
    }


}

