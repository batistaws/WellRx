package batista.WellRx.farmacia.controller;

import batista.WellRx.farmacia.dto.AtualizacaoFarmaceuticoDto;
import batista.WellRx.farmacia.dto.CadastroFarmaceuticoDto;
import batista.WellRx.farmacia.dto.ListagemFarmaceuticoDto;
import batista.WellRx.farmacia.dto.ListarFarmaceuticoDto;
import batista.WellRx.farmacia.service.FarmaceuticoService;
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
@RequestMapping("/farmaceuticos")
public class FarmaceuticoController {

    private final FarmaceuticoService service;

    public FarmaceuticoController(FarmaceuticoService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemFarmaceuticoDto> cadastrar(@RequestBody @Valid CadastroFarmaceuticoDto dto, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado){
        var farmaceutico = service.cadastrar(dto, logado);
        var uri = componentsBuilder.path("/farmaceuticos/{nomeCompleto}").buildAndExpand(farmaceutico.id()).toUri();
        return ResponseEntity.created(uri).body(farmaceutico);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ListarFarmaceuticoDto>>listar(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao, @AuthenticationPrincipal Usuario logado) {
        var pagina = service.listar(paginacao, logado);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<ListagemFarmaceuticoDto> detalhar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var farmaceutico = service.detalhar(id,logado);
        return ResponseEntity.ok(farmaceutico);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ListagemFarmaceuticoDto> atualizar(@RequestBody @Valid AtualizacaoFarmaceuticoDto dto, @AuthenticationPrincipal Usuario logado) {
        var farmaceutico = service.atualizar(dto, logado);
        return ResponseEntity.ok(farmaceutico);

    }
}
