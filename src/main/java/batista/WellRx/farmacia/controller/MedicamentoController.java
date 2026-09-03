package batista.WellRx.farmacia.controller;

import batista.WellRx.farmacia.dto.CadastroMedicamentoDto;
import batista.WellRx.farmacia.dto.ListagemMedicamentoDto;
import batista.WellRx.farmacia.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<ListagemMedicamentoDto> cadastrar(
            @RequestBody @Valid CadastroMedicamentoDto dto,
            UriComponentsBuilder componentsBuilder) {

        var medicamento = service.cadastrar(dto);
        var uri = componentsBuilder.path("/medicamentos/{id}").buildAndExpand(medicamento.id()).toUri();
        return ResponseEntity.created(uri).body(medicamento);
    }

    @PreAuthorize("hasAnyRole('FARMACEUTICO', 'MEDICO')")
    @GetMapping("/detalhar/{id}")
    public ResponseEntity<ListagemMedicamentoDto> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PreAuthorize("hasAnyRole('FARMACEUTICO', 'MEDICO')")
    @GetMapping("/listar")
    public ResponseEntity<Page<ListagemMedicamentoDto>> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 20) Pageable paginacao) {

        return ResponseEntity.ok(service.listar(nome, paginacao));
    }
}
