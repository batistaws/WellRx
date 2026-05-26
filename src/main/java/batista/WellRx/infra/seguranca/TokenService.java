package batista.WellRx.infra.seguranca;

import batista.WellRx.infra.exeption.RegraNegocioException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class  TokenService {

    @Value("${jwt.expiration}")
    private long tokenExpiracao;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiracao;

    @Value("${jwt.key}")
    private String key;



    public String gerarToken(Authentication authentication) {
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
             username = userDetails.getUsername();
        }else {
             username = principal.toString();
        }
        return buildToken(username);
    }

    private String buildToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiracao);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private String buildRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiracao);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String gerarRefreshToken(Authentication authentication) {
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }else {
            username = principal.toString();
        }
        return buildRefreshToken(username);
    }

    public boolean validarToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String validacaoToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new RegraNegocioException("Token inválido ou expirado!");
        }
    }
    //extrair innformações do token
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RegraNegocioException("Token inválido ou expirado!");
        }
    }

    private Instant dataExpiracao(int minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }
}
