package batista.WellRx.shared.service;

import batista.WellRx.shared.database.model.Usuario;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HierarquiaService {

    private final RoleHierarchy roleHierarchy;

    public HierarquiaService(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }


    public boolean usuarioNaoTemPermissao(Usuario logado, Usuario autor, String perfilDesejado) {
        return logado.getAuthorities().stream()
                .flatMap(autoridade -> roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade)).stream())
                .noneMatch(perfil -> perfil.getAuthority().equals(perfilDesejado) || logado.getId().equals(autor.getId()));
        // se for dele os dados ele podera fazer a requisição
    }

    public boolean usuarioTemPermissao(Usuario logado, String perfilDesejado) {
        return logado.getAuthorities().stream()
                .flatMap(auth -> roleHierarchy.getReachableGrantedAuthorities(List.of(auth)).stream())
                .anyMatch(p -> p.getAuthority().equals(perfilDesejado));
    }
}