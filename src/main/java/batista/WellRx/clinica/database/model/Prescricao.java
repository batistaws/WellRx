package batista.WellRx.clinica.database.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "prescricoes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao;

    private String observacoes;

    @OneToMany(mappedBy = "prescricao", cascade = CascadeType.ALL)
    private List<ItemPrescricao> itemPrescricaos = new ArrayList<>();

    public Prescricao(Atendimento atendimento, String observacoes) {
        this.atendimento = atendimento;
        this.observacoes = observacoes;
        this.dataEmissao = LocalDateTime.now();
    }

    public void adicionarItemPrescricao(ItemPrescricao itemPrescricao) {
        this.itemPrescricaos.add(itemPrescricao);
    }
}
