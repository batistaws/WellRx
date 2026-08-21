package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prontuarios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL)
    private List <Atendimento> atendimentos = new ArrayList<>();

    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL)
    private List<Alergia> alergias = new ArrayList<>();

    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL)
    private List<Comorbidade> comorbidades = new ArrayList<>();

    public Prontuario(Paciente paciente) {
        this.paciente = paciente;
        this.dataCriacao = LocalDate.now();
    }

    public void adicionarAtendimento(Atendimento atendimento) {
        this.atendimentos.add(atendimento);
    }

    public void adicionarAlergia(Alergia alergia) {
        this.alergias.add(alergia);
    }

    public void adicionarComorbidade(Comorbidade comorbidade) {
        this.comorbidades.add(comorbidade);
    }
}
