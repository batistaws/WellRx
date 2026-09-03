package batista.WellRx.farmacia.controller;

import batista.WellRx.farmacia.dto.ListagemEstoqueDto;
import batista.WellRx.farmacia.dto.ReposicaoEstoqueDto;
import batista.WellRx.farmacia.service.EstoqueService;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/medicamento/{medicamentoId}/repor")
    public ResponseEntity<ListagemEstoqueDto> reporEstoque(@PathVariable Long medicamentoId, @RequestBody @Valid ReposicaoEstoqueDto dto, @AuthenticationPrincipal Usuario logado) {
        var estoque = service.reporEstoque(medicamentoId, dto, logado);
        return ResponseEntity.ok(estoque);
    }

    @PreAuthorize("hasRole('FARMACEUTICO')")
    @GetMapping("/medicamento/{medicamentoId}")
    public ResponseEntity<ListagemEstoqueDto> detalhar(@PathVariable Long medicamentoId) {
        return ResponseEntity.ok(service.detalharPorMedicamento(medicamentoId));
    }
}
