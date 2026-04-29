package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {


    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ListarMedicoDto>>listarMedico(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListagemMedicoDto> detalharMedico(@PathVariable Long id) {
        var medico = service.listarPorId(id);
        return ResponseEntity.ok(new ListagemMedicoDto(medico));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemMedicoDto> cadastrar(@RequestBody @Valid CadastroMedicoDto dto, UriComponentsBuilder componentsBuilder) {
        var medico = service.cadastrar(dto);
        var uri = componentsBuilder.path("/medicos/{nomeCompleto}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemMedicoDto(medico));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemMedicoDto> atualizar(@RequestBody @Valid AtualizacaoMedicoDto dto) {
        var medico = service.atualizar(dto);
        return ResponseEntity.ok(new ListagemMedicoDto(medico));

    }
}
