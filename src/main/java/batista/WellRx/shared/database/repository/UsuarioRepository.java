package batista.WellRx.shared.database.repository;

import batista.WellRx.shared.database.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


}
