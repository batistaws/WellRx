package batista.WellRx.farmacia.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "principio_ativo")
    private String principioAtivo;

    private Boolean controlado;

    @Column(name = "unidade_medida")
    private String unidadeMedida;

    public Medicamento(String nome, String principioAtivo, Boolean controlado, String unidadeMedida) {
        this.nome = nome;
        this.principioAtivo = principioAtivo;
        this.controlado = controlado;
        this.unidadeMedida = unidadeMedida;
    }
}
