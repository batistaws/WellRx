package batista.WellRx.clinica.service;


import batista.WellRx.clinica.dto.ListarAtendimentosDto;
import batista.WellRx.clinica.database.repository.*;
import batista.WellRx.clinica.dto.CadastroAtendimentoDto;
import batista.WellRx.clinica.database.model.Atendimento;
import batista.WellRx.clinica.database.model.Consulta;
import batista.WellRx.clinica.database.model.SinaisVitais;
import batista.WellRx.clinica.dto.ListagemAtendimentoDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.database.repository.UsuarioRepository;
import batista.WellRx.shared.service.HierarquiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AtendimentoService {

    private final HierarquiaService hierarquiaService;
    private final AtendimentoRepository atendimentoRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final SinaisVitaisRepository sinaisVitaisRepository;


    public AtendimentoService(HierarquiaService hierarquiaService, AtendimentoRepository atendimentoRepository, MedicoRepository medicoRepository, ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository, ProntuarioRepository prontuarioRepository, SinaisVitaisRepository sinaisVitaisRepository) {
        this.hierarquiaService = hierarquiaService;
        this.atendimentoRepository = atendimentoRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;

        this.prontuarioRepository = prontuarioRepository;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
    }

    public Atendimento cadastrar(CadastroAtendimentoDto dto, Usuario logado, Long consultaId) {

        var consulta = consultaRepository.findById(consultaId).orElseThrow(() -> new RegraNegocioException("Consulta não encontrada"));
        if (!hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO")) {
            throw new RegraNegocioException("Acesso negado: Apenas medicos podem realizar atendimentos.");
        }

        validarMedicoPodeAtender(consulta, logado);

        var prontuario = prontuarioRepository.findByPacienteId(consulta.getPaciente().getId()).orElseThrow(() -> new RegraNegocioException("Prontuario não encontrado"));

        var atendimento = new Atendimento(dto, prontuario, consulta,consulta.getMedico());
        atendimentoRepository.save(atendimento);
        if (dto.sinaisVitais() != null) {
            var sinaisVitais = new SinaisVitais(atendimento,
                    dto.sinaisVitais().peso(),
                    dto.sinaisVitais().altura(),
                    dto.sinaisVitais().pressaoArterial(),
                    dto.sinaisVitais().temperatura(),
                    dto.sinaisVitais().frequenciaCardiaca());
            sinaisVitaisRepository.save(sinaisVitais);
            atendimento.setSinaisVitais(sinaisVitais);


        }
        return atendimento;
    }

    private void validarMedicoPodeAtender(Consulta consulta, Usuario logado) {
        var autorMedico = consulta.getMedico().getUsuario();

        if (hierarquiaService.usuarioNaoTemPermissao(logado, autorMedico, "ROLE_MEDICO")) {
            throw new RegraNegocioException("Acesso negado: Apenas o médico responsável pela consulta pode realizar o atendimento.");
        }
    }

    public Page<ListarAtendimentosDto> listar(Pageable paginacao, Usuario logado) {
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN")) {
            return atendimentoRepository.findAll(paginacao).map(ListarAtendimentosDto::new);
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")) {
            return atendimentoRepository.findAll(paginacao).map(ListarAtendimentosDto::new);
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO")) {
            var medico = medicoRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));
            var atendimento = atendimentoRepository.findByMedicoId(medico.getId(), paginacao).map(ListarAtendimentosDto::new);
            if (atendimento.isEmpty()) {
                throw new RegraNegocioException("Nenhum atendimento encontrado para este médico.");
            }else {
                return atendimento;
            }
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_PACIENTE")) {
            var paciente = pacienteRepository.findByUsuarioId(logado.getId())
                    .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
            return atendimentoRepository.findByProntuario_Paciente_Id(paciente.getId(), paginacao).map(ListarAtendimentosDto::new);
        }

        throw new RegraNegocioException("Acesso negado.");
    }

    public Page<ListarAtendimentosDto> listarPorId(Long id, Pageable paginacao, Usuario logado) {

        var atendimentos = atendimentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado"));

        boolean podeConsultar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")
                || logado.getId().equals(id);

        if (!podeConsultar) {
            throw new RegraNegocioException("Acesso negado.");
        }

        var paciente = pacienteRepository.findByUsuarioId(id)
                .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));

        return atendimentoRepository.findByProntuario_Paciente_Id(paciente.getId(), paginacao)
                .map(ListarAtendimentosDto::new);


    }
    public ListagemAtendimentoDto detalhar(Long id, Usuario logado) {

        var atendimento = atendimentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Atendimento não encontrado"));

        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_ADMIN")) {
            return new ListagemAtendimentoDto(atendimento);
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")) {
            return new ListagemAtendimentoDto(atendimento);
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_MEDICO")) {
            var medico = medicoRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));
            if (!atendimento.getMedico().getId().equals(medico.getId())) {
                throw new RegraNegocioException("Acesso negado: Apenas o médico responsável pelo atendimento pode visualizar os detalhes.");
            }
            return new ListagemAtendimentoDto(atendimento);
        }
        if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_PACIENTE")) {
            var paciente = pacienteRepository.findByUsuarioId(logado.getId())
                    .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
            if (!atendimento.getProntuario().getPaciente().getId().equals(paciente.getId())) {
                throw new RegraNegocioException("Acesso negado: Apenas o paciente responsável pelo atendimento pode visualizar os detalhes.");
            }
            return new ListagemAtendimentoDto(atendimento);
        }

        throw new RegraNegocioException("Acesso negado.");
    }


}
