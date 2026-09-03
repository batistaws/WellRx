package batista.WellRx.farmacia.controller;

import batista.WellRx.farmacia.dto.DispensacaoDto;
import batista.WellRx.farmacia.dto.ListagemDispensacaoDto;
import batista.WellRx.farmacia.service.DispensacaoService;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/dispensacao")
public class DispensasaoController {

    private final DispensacaoService service;

    public DispensasaoController(DispensacaoService service) {
        this.service = service;
    }

    @PostMapping("/itens-prescricao/{itemPrescricaoId}/dispensacao")
    public ResponseEntity<ListagemDispensacaoDto>dispensar(@RequestBody @Valid DispensacaoDto dto, @PathVariable Long itemPrescricaoId, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado) {
        var dispensacao = service.dispensar(itemPrescricaoId,dto, logado );
        var uri = componentsBuilder.path("/dispensacao/{id}").buildAndExpand(dispensacao.id()).toUri();
        return ResponseEntity.ok(dispensacao);
    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<ListagemDispensacaoDto> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }
}
