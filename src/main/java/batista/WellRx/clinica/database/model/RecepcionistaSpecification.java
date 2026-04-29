package batista.WellRx.clinica.database.model;

import batista.WellRx.shared.database.model.Usuario;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RecepcionistaSpecification {



    public static Specification<Recepcionista> estaAtivo() {
        return (root, query, builder) -> {
            Join<Recepcionista, Usuario> usuarioJoin = root.join("usuario");
            return builder.isTrue(usuarioJoin.get("ativo"));
        };
    }
}
