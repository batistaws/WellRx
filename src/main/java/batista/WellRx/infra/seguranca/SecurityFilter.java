package batista.WellRx.infra.seguranca;

import batista.WellRx.shared.database.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//filtro q filtra as requisições uma vez a cada requisiçao
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //recuperar o token da requisição
        //para ver se o cliente enviou um token JWT no cabeçalho
        String token = recuperarTokenRequicao(request);
        if (token != null){
            //validação do token
            //vai ser responsavel por validar o token e autenticar o usuario no spring security
            String username = tokenService.verificarToken(token);
            var user = usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username).orElseThrow();

            var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

            //como API REST é stateles, não fica "gravado" o usuário q esta usando requisições, isso vai servir para q o token seja vereficado a cada requisiçaõ
            SecurityContextHolder.getContext().setAuthentication(authentication);

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
