package batista.WellRx.clinica.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sinais_vitais")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SinaisVitais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    private Double peso;
    private Double altura;
    private Double temperatura;

    @Column(name = "pressao_arterial")
    private String pressaoArterial;

    @Column(name = "frequencia_cardiaca")
    private Integer frequenciaCardiaca;


    public SinaisVitais(Atendimento atendimento, Double peso, Double altura, String pressaoArterial, Double temperatura, Integer frequenciaCardiaca) {
        this.atendimento = atendimento;
        this.peso = peso;
        this.altura = altura;
        this.pressaoArterial = pressaoArterial;
        this.temperatura = temperatura;
        this.frequenciaCardiaca = frequenciaCardiaca;
    }
}
