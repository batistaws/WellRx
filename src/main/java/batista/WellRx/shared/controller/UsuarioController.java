package batista.WellRx.shared.controller;

import batista.WellRx.infra.seguranca.TokenService;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.dto.DadosRefreshTokenDTO;
import batista.WellRx.shared.dto.DadosTokenDto;
import batista.WellRx.shared.dto.EfetuarLoginDto;
import batista.WellRx.shared.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/wellrx")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository, AuthenticationManager manager, TokenService tokenService) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<DadosTokenDto> login(@RequestBody @Valid EfetuarLoginDto dto) {

        var authenticationManager = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
        var authentication = manager.authenticate(authenticationManager) ;

        var token = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        var refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenDto(token, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosTokenDto>atualizarToken(@Valid @RequestBody DadosRefreshTokenDTO dto){
        try {
            var refreshToken = dto.refreshToken();
            Long idUsuario = Long.valueOf(tokenService.verificarToken(refreshToken));
            var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

            var token = tokenService.gerarToken(usuario);
            var tokenAtualizacao = tokenService.gerarRefreshToken(usuario);
            return ResponseEntity.ok(new DadosTokenDto(token,tokenAtualizacao));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Or return a custom error message
        }
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
