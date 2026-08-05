package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
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
@Table(name = "comorbidades")
public class Comorbidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    private String condicao;

    @Column(name = "data_diagnostico")
    private LocalDate dataDiagnostico;

    public Comorbidade(Prontuario prontuario, String condicao, LocalDate dataDiagnostico) {
        this.prontuario = prontuario;
        this.condicao = condicao;
        this.dataDiagnostico = dataDiagnostico;
        prontuario.adicionarComorbidade(this);
    }
}
