package batista.WellRx.farmacia.database.model;

import batista.WellRx.infra.exeption.RegraNegocioException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "estoques")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    @Column(name = "quantidade_disponivel")
    private Integer quantidadeDisponivel;

    public Estoque(Medicamento medicamento, Integer quantidadeDisponivel) {
        this.medicamento = medicamento;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void adicionar(Integer quantidade) {
        this.quantidadeDisponivel += quantidade;
    }

    public void remover(Integer quantidade) {
        if (quantidade > this.quantidadeDisponivel) {
            throw new RegraNegocioException("Quantidade a remover é maior que a disponível no estoque.");
        }
        this.quantidadeDisponivel -= quantidade;
    }
}
