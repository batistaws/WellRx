package batista.WellRx.infra.seguranca;

import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//filtro q filtra as requisições uma vez a cada requisiçao
public class SecurityFilter extends OncePerRequestFilter {

    @Lazy
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if (tokenService.validarToken(token)){
                String username = tokenService.getUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        //pode passar a proxima parte para o proximo filter ou pro proximo controlador
        filterChain.doFilter(request,response);
    }

    private String recuperarTokenRequicao(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");
        if (authorization != null){
            return authorization.replace("Bearer ", "");

        }
        return null;
    }
}
