package batista.WellRx.clinica.controller;


import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.service.PacienteService;
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
@RequestMapping("/pacientes")
public class PacienteController {


    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('_RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<Page<ListarPacienteDto>>listarPaciente(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listar(paginacao,logado);
        return ResponseEntity.ok(pagina);
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ListagemPacienteDto> detalharPaciente(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var paciente = service.listarPorId(id,logado);
        return ResponseEntity.ok(new ListagemPacienteDto(paciente));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemPacienteDto> cadastrar(@RequestBody @Valid CadastroPacienteDto dto, UriComponentsBuilder componentsBuilder) {
        var paciente = service.cadastrar(dto);
        var uri = componentsBuilder.path("/pacientes/{nomeCompleto}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemPacienteDto(paciente));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemPacienteDto> atualizar(@RequestBody @Valid AtualizacaoPacienteDto dto, @AuthenticationPrincipal Usuario logado) {
        var paciente = service.atualizar(dto, logado);
        return ResponseEntity.ok(new ListagemPacienteDto(paciente));
    }
}