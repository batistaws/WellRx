package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resultados_exame")
public class ResultadoExame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "exame_id")
    private Exame exame;

    @Column(name = "data_resultado")
    private LocalDateTime dataResultado;

    private String laudo;

    public ResultadoExame(Exame exame, String laudo) {
        this.exame = exame;
        this.dataResultado = LocalDateTime.now();
        this.laudo = laudo;
        exame.setStatus(StatusExame.REALIZADO);
    }
}
