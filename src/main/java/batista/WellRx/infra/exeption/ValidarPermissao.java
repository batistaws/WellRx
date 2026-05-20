package batista.WellRx.infra.exeption;

import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.service.HierarquiaService;
import org.springframework.stereotype.Component;

@Component
public class ValidarPermissao {

    private final HierarquiaService hierarquiaService;

    public ValidarPermissao(HierarquiaService hierarquiaService) {
        this.hierarquiaService = hierarquiaService;
    }

    public void validarDonoOuAdmin(Long idUsuarioAlvo, Usuario logado, String msg) {
        boolean isAdmin = hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN");
        boolean isDono = logado.getId().equals(idUsuarioAlvo);

        System.out.println("--- DEBUG PERMISSÃO ---");
        System.out.println("ID Logado: " + logado.getId() + " (" + logado.getId().getClass().getSimpleName() + ")");
        System.out.println("ID Alvo: " + idUsuarioAlvo + " (" + idUsuarioAlvo.getClass().getSimpleName() + ")");
        System.out.println("isAdmin: " + isAdmin);
        System.out.println("isDono: " + isDono);
        System.out.println("-----------------------");

        if (!isAdmin && !isDono) {
            throw new RegraNegocioException(msg);
        }
    }
}
