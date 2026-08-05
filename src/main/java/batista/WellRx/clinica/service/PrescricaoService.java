package batista.WellRx.clinica.service;

import batista.WellRx.clinica.dto.ListarPrescricaoDto;
import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.database.repository.PacienteRepository;
import batista.WellRx.clinica.dto.CadastroPrescricaoDto;
import batista.WellRx.clinica.dto.ListagemPrescricaoDto;
import batista.WellRx.clinica.database.repository.PrescricaoRepository;
import batista.WellRx.clinica.database.model.ItemPrescricao;
import batista.WellRx.clinica.database.model.Prescricao;
import batista.WellRx.clinica.database.repository.AtendimentoRepository;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.service.HierarquiaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PrescricaoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PrescricaoRepository prescricaoRepository;
    private final HierarquiaService hierarquiaService;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public PrescricaoService(AtendimentoRepository atendimentoRepository, PrescricaoRepository prescricaoRepository, HierarquiaService hierarquiaService, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.prescricaoRepository = prescricaoRepository;
        this.hierarquiaService = hierarquiaService;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public ListagemPrescricaoDto cadastrar(@Valid CadastroPrescricaoDto dto, Usuario logado, Long idAtendimento) {

        var atendimento = atendimentoRepository.findById(idAtendimento).orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        if (!atendimento.getConsulta().getMedico().getUsuario().getId().equals(logado.getId())) {
            throw new RuntimeException("Você não tem permissão para cadastrar prescrição para este atendimento");
        }

        var prescricao = new Prescricao(atendimento, dto.observacao());
        prescricaoRepository.save(prescricao);

        dto.itens().forEach(itemDto -> {
            new ItemPrescricao(prescricao, itemDto.medicamento(), itemDto.dosagem(), itemDto.posologia(), itemDto.duracaoDias());

        });
        return new ListagemPrescricaoDto(prescricao);
    }

    public ListagemPrescricaoDto detalhar(Long id, Usuario logado) {

        var prescricao = prescricaoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Prescrição não encontrada"));

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_ADMIN")) {
            return new ListagemPrescricaoDto(prescricao);
        }
        if (!prescricao.getAtendimento().getConsulta().getMedico().getUsuario().getId().equals(logado.getId()) && !prescricao.getAtendimento().getProntuario().getPaciente().getUsuario().getId().equals(logado.getId())) {
            throw new RegraNegocioException("Você não tem permissão para visualizar esta prescrição");
        }

        return new ListagemPrescricaoDto(prescricao);
    }

    public Page<ListarPrescricaoDto> listar(Usuario logado, Pageable paginacao) {

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_ADMIN")) {
            return prescricaoRepository.findAll(paginacao).map(ListarPrescricaoDto::new);
        }

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_MEDICO")) {
            var medico = medicoRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado"));
            return prescricaoRepository.findByAtendimento_Medico_Id(medico.getId(), paginacao).map(ListarPrescricaoDto::new);
        }

        if (hierarquiaService.usuarioTemPermissao(logado,"ROLE_PACIENTE")) {
            var paciente = pacienteRepository.findByUsuarioId(logado.getId()).orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));
            return prescricaoRepository.findByAtendimento_Prontuario_Paciente_Id(paciente.getId(), paginacao).map(ListarPrescricaoDto::new);
        }

        throw new RegraNegocioException("Você não tem permissão para listar prescrições");
    }

    public Page<ListarPrescricaoDto> listarPorId(Usuario logado, Pageable paginacao, Long usuarioId) {
        boolean podeConsultar = hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")
                || logado.getId().equals(usuarioId);

        if (!podeConsultar) {
            throw new RegraNegocioException("Acesso negado.");
        }

        var paciente = pacienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado"));

        return prescricaoRepository.findByAtendimento_Prontuario_Paciente_Id(paciente.getId(), paginacao)
                .map(ListarPrescricaoDto::new);
    }

}
