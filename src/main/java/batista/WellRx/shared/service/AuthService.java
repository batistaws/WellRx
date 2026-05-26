package batista.WellRx.shared.service;

import batista.WellRx.clinica.dto.DadosPerfil;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.dto.TokenResponseDto;
import batista.WellRx.infra.seguranca.TokenService;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.PerfilRepository;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.dto.EfetuarLoginDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    @Lazy
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Value("${jwt.expiration}")
    private long expiracaoTime;

    public AuthService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

//    @Transactional
//    public TokenResponseDto login(EfetuarLoginDto dto) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha())
//            );
//
//            String token = tokenService.gerarToken(authentication);
//
//            return new TokenResponseDto(token, expiracaoTime);
//        } catch (RegraNegocioException e) {
//            throw new RegraNegocioException("Login ou senha inválidos!");
//        }
//
//    }

    @Transactional
    public void verificarEmail(String token) {
        var usuario = usuarioRepository.findByToken(token).orElseThrow();
        usuario.verificar();
    }

    @Transactional
    public Usuario adicionarPerfil(@Valid DadosPerfil dto, Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        var perfil = perfilRepository.findByNome(dto.perfilNome());
        usuario.adicionarPerfil(perfil);
        return usuario;
    }

    @Transactional
    public Usuario removerPerfil(@Valid DadosPerfil dto, Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        var perfil = perfilRepository.findByNome(dto.perfilNome());
        usuario.removerPerfil(perfil);
        return usuario;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return usuarioRepository.findByCpfAndVerificadoTrue(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }
}
