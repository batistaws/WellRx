package batista.WellRx.clinica.service;


import batista.WellRx.clinica.database.repository.*;
import batista.WellRx.clinica.dto.*;
import batista.WellRx.clinica.database.model.Exame;
import batista.WellRx.clinica.database.model.ResultadoExame;
import batista.WellRx.clinica.database.model.StatusExame;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.service.HierarquiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ExameService {

    private final AtendimentoRepository atendimentoRepository;
    private final ExameRepository exameRepository;
    private final HierarquiaService hierarquiaService;
    private final ResultadoExameRepository resultadoExameRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ExameService(AtendimentoRepository atendimentoRepository, ExameRepository exameRepository, HierarquiaService hierarquiaService, ResultadoExameRepository resultadoExameRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.exameRepository = exameRepository;
        this.hierarquiaService = hierarquiaService;
        this.resultadoExameRepository = resultadoExameRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public ListagemExameDto cadastrar(CadastroExameDto dto, Usuario logado, Long atendimentoId) {

        var atendimento = atendimentoRepository.findById(atendimentoId).orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (!hierarquiaService.usuarioTemPermissao(logado,"ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Médico não pode cadastrar exames.");
        }

        var exames = new Exame(atendimento, dto.tipoExame(), dto.dataAgendada());
        exameRepository.save(exames);
        return new ListagemExameDto(exames);
    }


    @Transactional
    public ListagemExameDto lancarResultado(Long exameId, LancarResultadoExameDto dto, Usuario logado) {

        boolean podeLancar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO");

        if (!podeLancar) {
            throw new RegraNegocioException("Apenas médico ou admin podem lançar resultado de exame.");
        }

        var exame = exameRepository.findById(exameId)
                .orElseThrow(() -> new RegraNegocioException("Exame não encontrado"));

        if (exame.getStatus() != StatusExame.AGENDADO) {
            throw new RegraNegocioException("Só é possível lançar resultado de exames com status AGENDADO.");
        }

        var resultado = new ResultadoExame(exame, dto.laudo());
        resultadoExameRepository.save(resultado);
        exame.setStatus(StatusExame.REALIZADO);
        exame.setResultadoExame(resultado);

        return new ListagemExameDto(exame);
    }

    @Transactional
    public ListagemExameDto cancelar(Long exameId, Usuario logado) {

        var exame = exameRepository.findById(exameId)
                .orElseThrow(() -> new RegraNegocioException("Exame não encontrado"));

        if (!exame.getAtendimento().getMedico().getUsuario().getId().equals(logado.getId()) ) {
            throw new RegraNegocioException("Apenas o médico responsável pode cancelar este exame.");
        }

        if (exame.getStatus() == StatusExame.REALIZADO) {
            throw new RegraNegocioException("Não é possível cancelar um exame já realizado.");
        }

        exame.cancelar();
        return new ListagemExameDto(exame);
    }

    public ListagemExameDto detalhar(Long id) {
        var exame = exameRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Exame não encontrado"));
        return new ListagemExameDto(exame);
    }

    public Page<ListarExameDto> listar(Usuario logado, Pageable paginacao) {

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_ADMIN")) {
            return exameRepository.findAll(paginacao).map(ListarExameDto::new);
        }

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_MEDICO")) {
            var medico = medicoRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));
            return exameRepository.findByAtendimento_Medico_Id(medico.getId(), paginacao).map(ListarExameDto::new);
        }

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_PACIENTE")) {
            var paciente = pacienteRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
            return exameRepository.findByAtendimento_Prontuario_Paciente_Id(paciente.getId(), paginacao).map(ListarExameDto::new);
        }

        throw new RegraNegocioException("Você não tem permissão para listar exames");
    }


    public Page<ListarExameDto> listarPorUsuarioId(Long usuarioId, Usuario logado, Pageable paginacao) {

        boolean podeConsultar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")
                || logado.getId().equals(usuarioId);

        if (!podeConsultar){
            throw new RegraNegocioException("Acesso negado.");
        }

        var paciente = pacienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));

        return exameRepository.findByAtendimento_Prontuario_Paciente_Id(paciente.getId(), paginacao).map(ListarExameDto::new);

    }
}

