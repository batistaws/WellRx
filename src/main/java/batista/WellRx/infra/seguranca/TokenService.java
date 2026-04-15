package batista.WellRx.infra.seguranca;

import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("1234678");
            return JWT.create()
                    .withIssuer("WellRx")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expiracao(50))
                    .sign(algorithm);
        } catch (
                JWTCreationException exception) {
            throw new RegraNegocioException("Erro ao gerar um token JWT de acesso!");
        }
    }
    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("1234678");
            return JWT.create()
                    .withIssuer("WellRx")
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(expiracao(120))
                    .sign(algorithm);
        } catch (
                JWTCreationException exception) {
            throw new RegraNegocioException("Erro ao gerar um token JWT de acesso!");
        }
    }
    public String verificarToken(String token){
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256("1234678");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("WellRx")
                    .build();

            //vai decodificar o token e validar as keys e o issuer e retornar o username de acordo com o usuário do getPrincipal
            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            throw new RegraNegocioException("Erro ao verificar um token JWT de acesso!");
        }
    }

    //método de expiração do token
    private Instant expiracao(int minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }


}
