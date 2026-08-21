package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.database.repository.ExameRepository;
import batista.WellRx.clinica.dto.CadastroExameDto;
import batista.WellRx.clinica.dto.LancarResultadoExameDto;
import batista.WellRx.clinica.dto.ListagemExameDto;
import batista.WellRx.clinica.dto.ListarExameDto;
import batista.WellRx.clinica.service.ExameService;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/exames")
public class ExameController {

    private final ExameService service;
    private final ExameRepository repository;

    public ExameController(ExameService service, ExameRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PreAuthorize("hasRole('RECEPCIONISTA')")
    @PostMapping("/atendimentos/{atendimentoId}/exames")
    public ResponseEntity<ListagemExameDto> cadastrarExame(@AuthenticationPrincipal Usuario logado, @RequestBody @Valid CadastroExameDto dto, @PathVariable Long atendimentoId, UriComponentsBuilder componentsBuilder) {
        var exame = service.cadastrar(dto, logado, atendimentoId);
        var uri = componentsBuilder.path("/exames/{id}").buildAndExpand(exame.id()).toUri();
        return ResponseEntity.created(uri).body(exame);
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/{id}/resultado")
    public ResponseEntity<ListagemExameDto> lancarResultado(@PathVariable Long id, @RequestBody @Valid LancarResultadoExameDto dto, @AuthenticationPrincipal Usuario logado) {
        var exame = service.lancarResultado(id, dto, logado);
        return ResponseEntity.ok(exame);
    }

    @PreAuthorize("hasRole('RECEPCIONISTA')")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ListagemExameDto> cancelar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var exame = service.cancelar(id, logado);
        return ResponseEntity.ok(exame);
    }

    @PreAuthorize("hasRole('RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ListagemExameDto> detalhar(@PathVariable Long id) {
        var exame = service.detalhar(id);
        return ResponseEntity.ok(exame);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ListarExameDto>> listar(@PageableDefault(size = 20) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var exame = service.listar(logado, paginacao);
        return ResponseEntity.ok(exame);
    }

    @PreAuthorize("hasRole('RECEPCIONISTA')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<ListarExameDto>> listarPorUsuarioId(@PathVariable Long usuarioId, @PageableDefault(size = 20) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var exame = service.listarPorUsuarioId(usuarioId, logado, paginacao);
        return ResponseEntity.ok(exame);
    }
}

