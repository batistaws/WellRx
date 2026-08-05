package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alergias")
@AllArgsConstructor
public class Alergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    private String substancia;

    @Enumerated(EnumType.STRING)
    private GravidadeAlergia gravidade;

    public Alergia(Prontuario prontuario, String substancia, GravidadeAlergia gravidade) {
        this.prontuario = prontuario;
        this.substancia = substancia;
        this.gravidade = gravidade;
        prontuario.adicionarAlergia(this);
    }
}
