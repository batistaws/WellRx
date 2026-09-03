package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.service.MedicoService;
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
@RequestMapping("/medicos")
public class MedicoController {


    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemMedicoDto> cadastrar(@RequestBody @Valid CadastroMedicoDto dto, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado) {
        var medico = service.cadastrar(dto, logado);
        var uri = componentsBuilder.path("/medicos/{nomeCompleto}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemMedicoDto(medico));
    }

    @GetMapping
    public ResponseEntity<Page<ListarMedicoDto>>listarMedico(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listar(paginacao, logado);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListagemMedicoDto> detalharMedico(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var medico = service.listarPorId(id,logado);
        return ResponseEntity.ok(new ListagemMedicoDto(medico));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemMedicoDto> atualizar(@RequestBody @Valid AtualizacaoMedicoDto dto, @AuthenticationPrincipal Usuario logado) {
        var medico = service.atualizar(dto, logado);
        return ResponseEntity.ok(new ListagemMedicoDto(medico));

    }
}
