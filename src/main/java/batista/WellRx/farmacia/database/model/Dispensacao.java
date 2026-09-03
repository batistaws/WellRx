package batista.WellRx.farmacia.database.model;

import batista.WellRx.clinica.database.model.ItemPrescricao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dispensacoes")
public class Dispensacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_prescricao_id")
    private ItemPrescricao itemPrescricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmaceutico_id")
    private Farmaceutico farmaceutico;

    @Column(name = "quantidade_dispensada")
    private Integer quantidadeDispensada;

    @Column(name = "data_dispensacao")
    private LocalDateTime dataDispensacao;

    public Dispensacao(ItemPrescricao itemPrescricao, Farmaceutico farmaceutico, Integer quantidadeDispensada) {
        this.itemPrescricao = itemPrescricao;
        this.farmaceutico = farmaceutico;
        this.quantidadeDispensada = quantidadeDispensada;
        this.dataDispensacao = LocalDateTime.now();
    }
}
