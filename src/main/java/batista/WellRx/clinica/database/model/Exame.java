package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "exames")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    private String tipo;

    @Enumerated(EnumType.STRING)
    private StatusExame status;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_agendada")
    private LocalDateTime dataAgendamento;

    @OneToOne(mappedBy = "exame", cascade = CascadeType.ALL)
    private ResultadoExame resultadoExame;

    public Exame(Atendimento atendimento, String tipo, @NotNull(message = "A data do exame é obrigatória") LocalDateTime localDateTime) {
        this.atendimento = atendimento;
        this.tipo = tipo;
        this.status = StatusExame.AGENDADO  ;
        this.dataSolicitacao = LocalDateTime.now();
        this.dataAgendamento = localDateTime;
    }

    public void cancelar() {
        this.status = StatusExame.CANCELADO;
    }
}
