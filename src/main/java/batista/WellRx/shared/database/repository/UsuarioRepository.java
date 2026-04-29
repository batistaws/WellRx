package batista.WellRx.shared.database.repository;

import batista.WellRx.shared.database.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String username);

    Optional<Usuario> findByCpfAndVerificadoTrue(String cpf);

    Optional<Usuario> findByToken(String token);
}
