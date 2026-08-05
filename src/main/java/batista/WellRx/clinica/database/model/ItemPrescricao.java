package batista.WellRx.clinica.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "itens_prescricao")
public class ItemPrescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescricao_id")
    private Prescricao prescricao;

    private String medicamento;
    private String dosagem;
    private String posologia;

    @Column(name = "duracao_dias")
    private Integer duracaoDias;

    public ItemPrescricao(Prescricao prescricao, String medicamento, String dosagem,
                          String posologia, Integer duracaoDias) {
        this.prescricao = prescricao;
        this.medicamento = medicamento;
        this.dosagem = dosagem;
        this.posologia = posologia;
        this.duracaoDias = duracaoDias;
        prescricao.adicionarItemPrescricao(this);
    }
}
