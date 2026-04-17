package batista.WellRx.shared.database.repository;

import batista.WellRx.shared.database.model.Perfil;
import batista.WellRx.shared.database.model.PerfilEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(PerfilEnum nome);
}
