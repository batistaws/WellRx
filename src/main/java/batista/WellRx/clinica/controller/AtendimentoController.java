package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.CadastroAtendimentoDto;
import batista.WellRx.clinica.dto.ListagemAtendimentoDto;
import batista.WellRx.clinica.dto.ListarAtendimentosDto;
import batista.WellRx.clinica.service.AtendimentoService;
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
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/{consultaId}/atendimento")
    public ResponseEntity<ListagemAtendimentoDto> cadastrarAtendimento(@PathVariable Long consultaId, @RequestBody @Valid CadastroAtendimentoDto dto, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado) {
        var atendimento = service.cadastrar(dto, logado, consultaId);
        var uri = componentsBuilder.path("/atendimentos/{id}").buildAndExpand(atendimento.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemAtendimentoDto(atendimento));
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ListarAtendimentosDto>> listarAtendimentos(@PageableDefault(size = 20) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listar(paginacao, logado);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/listar/{porId}")
    public ResponseEntity<Page<ListarAtendimentosDto>> ListarAtendimentosPorId(@PathVariable Long id, @PageableDefault(size = 20) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listarPorId(id, paginacao, logado);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListagemAtendimentoDto> detalharAtendimento(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var atendimento = service.detalhar(id, logado);
        return ResponseEntity.ok(atendimento);
    }
}

