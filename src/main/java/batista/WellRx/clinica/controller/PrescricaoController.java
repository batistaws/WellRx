package batista.WellRx.clinica.controller;

import batista.WellRx.clinica.dto.CadastroPrescricaoDto;
import batista.WellRx.clinica.dto.ListagemPrescricaoDto;
import batista.WellRx.clinica.dto.ListarPrescricaoDto;
import batista.WellRx.clinica.service.PrescricaoService;
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
@RequestMapping("/prescricao")
public class PrescricaoController {


    private final PrescricaoService service;

    public PrescricaoController(PrescricaoService prescricaoService, PrescricaoService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/cadastrar/{idAtendimento}")
    public ResponseEntity<ListagemPrescricaoDto> cadastrar(@PathVariable Long idAtendimento, UriComponentsBuilder componentsBuilder, @AuthenticationPrincipal Usuario logado, @Valid @RequestBody CadastroPrescricaoDto dto) {
        var prescricao = service.cadastrar(dto, logado, idAtendimento);
        var uri = componentsBuilder.path("/prescricao/{id}").buildAndExpand(prescricao.id()).toUri();
        return ResponseEntity.created(uri).body(prescricao);
    }

    @PreAuthorize("hasRole('ROLE_MEDICO') or hasAuthority('ROLE_PACIENTE')")
    @GetMapping("/detalhar/{id}")
    public ResponseEntity<ListagemPrescricaoDto> detalhar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        var  prescricao = service.detalhar(id, logado);
        return ResponseEntity.ok(prescricao);
   }

   @GetMapping("/listar")
    public ResponseEntity<Page<ListarPrescricaoDto>> listar(@AuthenticationPrincipal Usuario logado, @PageableDefault(size = 20) Pageable paginacao){
        var pagina = service.listar(logado, paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/listar/{usuarioId}")
    public ResponseEntity<Page<ListarPrescricaoDto>> listarPorId(@PathVariable Long usuarioId, @AuthenticationPrincipal Usuario logado, @PageableDefault(size = 20) Pageable paginacao){
        var pagina = service.listarPorId(logado, paginacao, usuarioId);
        return ResponseEntity.ok(pagina);
    }
}
