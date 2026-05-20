package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.database.model.Recepcionista;
import batista.WellRx.clinica.dto.AtualizacaoRecepcionistaDto;
import batista.WellRx.clinica.dto.CadastroRecepcionistaDto;
import batista.WellRx.clinica.dto.ListagemRecepcionistaDto;
import batista.WellRx.clinica.dto.ListarRecepcionistaDto;
import batista.WellRx.clinica.service.RecepcionistaService;
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
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService service;

    public RecepcionistaController(RecepcionistaService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ListarRecepcionistaDto>>litarRecepcionistas(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        var pagina = service.listar(paginacao);
        return ResponseEntity.ok(pagina);
    }
    @PreAuthorize("hasRole('ROLE_RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ListagemRecepcionistaDto> detalharRecepcionista(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var recepcionista = service.listarPorId(id, logado);
        return ResponseEntity.ok(new ListagemRecepcionistaDto(recepcionista));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemRecepcionistaDto> cadastrar(@RequestBody @Valid CadastroRecepcionistaDto dto, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado) {
        var recepcionista = service.cadastrar(dto, logado);
        var uri = componentsBuilder.path("/recepcionistas/{nomeCompleto}").buildAndExpand(recepcionista.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListagemRecepcionistaDto(recepcionista));
    }

    @PreAuthorize("hasRole('ROLE_RECEPCIONISTA')")
    @PutMapping("/atualizar")
    public ResponseEntity<ListagemRecepcionistaDto> atualizar(@RequestBody @Valid AtualizacaoRecepcionistaDto dto, @AuthenticationPrincipal Usuario logado) {
        var recepcionista = service.atualizar(dto,logado);
        return ResponseEntity.ok(new ListagemRecepcionistaDto(recepcionista));
    }


}
