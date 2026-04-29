package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.AtualizacaoRecepcionistaDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.clinica.dto.ListagemRecepcionistaDto;
import batista.WellRx.clinica.dto.ListarRecepcionistaDto;
import batista.WellRx.clinica.service.RecepcionistaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService service;

    public RecepcionistaController(RecepcionistaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ListarRecepcionistaDto>>litarRecepcionistas(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListagemRecepcionistaDto> detalharRecepcionista(@PathVariable Long id) {
        var recepcionista = service.listarPorId(id);
        return ResponseEntity.ok(new ListagemRecepcionistaDto(recepcionista));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemRecepcionistaDto> cadastrar(@RequestBody @Valid CadastroRecepcionistaDto dto, UriComponentsBuilder componentsBuilder) {
        var recepcionista = service.cadastrar(dto);
        var uri = componentsBuilder.path("/recepcionistas/{nomeCompleto}").buildAndExpand(recepcionista.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemRecepcionistaDto(recepcionista));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemRecepcionistaDto> atualizar(@RequestBody @Valid AtualizacaoRecepcionistaDto dto) {
        var recepcionista = service.atualizar(dto);
        return ResponseEntity.ok(new ListagemRecepcionistaDto(recepcionista));
    }


}
