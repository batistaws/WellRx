package batista.WellRx.shared.controller;

import batista.WellRx.clinica.dto.DadosPerfil;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.dto.*;
import batista.WellRx.infra.seguranca.TokenService;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
public class AuthController {

    private final AuthService service;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AuthController(AuthService service, UsuarioRepository usuarioRepository, AuthenticationManager manager, TokenService tokenService) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.manager = manager;
        this.tokenService = tokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<DadosTokenDto> login(@RequestBody @Valid EfetuarLoginDto dto) {
        var authenticationManager = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
        var authentication = manager.authenticate(authenticationManager) ;

        var token = tokenService.gerarToken(authentication);
        var refreshToken = tokenService.gerarRefreshToken(authentication);

        return ResponseEntity.ok(new DadosTokenDto(token, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosTokenDto>atualizarToken(@Valid @RequestBody DadosRefreshTokenDTO dto){
        try {
            var refreshToken = dto.refreshToken();
            Long idUsuario = Long.valueOf(tokenService.validacaoToken((refreshToken)));
            var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

            var token = tokenService.gerarToken((Authentication) usuario);
            var tokenAtualizacao = tokenService.gerarRefreshToken((Authentication) usuario);
            return ResponseEntity.ok(new DadosTokenDto(token,tokenAtualizacao));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Or return a custom error message
        }
    }


    @PatchMapping("/adiconar-perfil/{id}")
    public ResponseEntity<ListagemUsuarioDto> adicionarPerfil(@RequestBody @Valid DadosPerfil dto, @PathVariable Long id) {
        var usuario = service.adicionarPerfil(dto, id);
        return ResponseEntity.ok(new ListagemUsuarioDto(usuario));
    }

    @PatchMapping("/remover-perfil/{id}")
    public ResponseEntity<ListagemUsuarioDto> removerPerfil(@RequestBody @Valid DadosPerfil dto, @PathVariable Long id) {
        var usuario = service.removerPerfil(dto, id);
        return ResponseEntity.ok(new ListagemUsuarioDto(usuario));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String token) {
        try {
            service.verificarEmail(token);
            return ResponseEntity.ok("Email verificado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }
}
