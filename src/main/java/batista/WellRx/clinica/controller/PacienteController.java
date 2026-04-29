package batista.WellRx.clinica.controller;


import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {


    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ListarPacienteDto>>listarPaciente(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListagemPacienteDto> detalharPaciente(@PathVariable Long id) {
        var paciente = service.listarPorId(id);
        return ResponseEntity.ok(new ListagemPacienteDto(paciente));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemPacienteDto> cadastrar(@RequestBody @Valid CadastroPacienteDto dto, UriComponentsBuilder componentsBuilder) {
        var paciente = service.cadastrar(dto);
        var uri = componentsBuilder.path("/pacientes/{nomeCompleto}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemPacienteDto(paciente));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemPacienteDto> atualizar(@RequestBody @Valid AtualizacaoPacienteDto dto) {
        var paciente = service.atualizar(dto);
        return ResponseEntity.ok(new ListagemPacienteDto(paciente));
    }
}