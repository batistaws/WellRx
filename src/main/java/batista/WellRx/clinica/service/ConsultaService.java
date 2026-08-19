package batista.WellRx.clinica.service;

import batista.WellRx.clinica.database.model.Consulta;
import batista.WellRx.clinica.database.repository.ConsultaRepository;
import batista.WellRx.clinica.database.model.Medico;
import batista.WellRx.clinica.database.repository.MedicoRepository;
import batista.WellRx.clinica.database.repository.PacienteRepository;
import batista.WellRx.clinica.dto.AgendamentoConsultaDto;
import batista.WellRx.clinica.dto.CancelamentoConsultaDto;
import batista.WellRx.clinica.dto.ListarConsultaDto;
import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.infra.exeption.validacoes.ValidadorAgendamentoConsulta;
import batista.WellRx.shared.database.model.Usuario;
import batista.WellRx.shared.service.HierarquiaService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static batista.WellRx.clinica.database.model.StatusConsulta.AGENDADA;

@Service
public class ConsultaService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;
    private final List<ValidadorAgendamentoConsulta> validadores;
    private final HierarquiaService hierarquiaService;

    public ConsultaService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository, ConsultaRepository consultaRepository, List<ValidadorAgendamentoConsulta> validadores, HierarquiaService hierarquiaService) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
        this.validadores = validadores;
        this.hierarquiaService = hierarquiaService;
    }

    @Transactional
    public Consulta agendar(AgendamentoConsultaDto dto, Usuario logado) {

        if (hierarquiaService.usuarioNaoTemPermissao(logado, pacienteRepository.findById(dto.idPaciente()).orElseThrow(() -> new RegraNegocioException("Id do paciente não existe")).getUsuario(), "ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Acesso negado: Apenas o paciente ou recepcionista podem agendar a consulta.");
        }

        if (dto.idMedico() != null && !medicoRepository.existsById(dto.idMedico())){
            throw new RegraNegocioException("Id do medico não existe");
        }

        validadores.forEach(v -> v.validar(dto));
        var paciente = pacienteRepository.findById(dto.idPaciente()).get();
        var medico = escolherMedico(dto);
        if (medico == null){
            throw new RegraNegocioException("Não existe medico disponivel nesta data");
        }
        var consulta = new Consulta(null, medico, paciente, dto.data(), AGENDADA, dto.motivoConsulta(),null );

        return consultaRepository.save(consulta);
    }

    private Medico escolherMedico(AgendamentoConsultaDto dto) {
        if (dto.idMedico() != null){
            return medicoRepository.getReferenceById(dto.idMedico());
        }
        if (dto.especialidade() == null){
            throw new RegraNegocioException("Especialidade é obrigatória quando o médico não for escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dto.especialidade(), dto.data());
    }

    public void cancelar(CancelamentoConsultaDto dto, Usuario logado) {

        var consulta = consultaRepository.findById(dto.idConsulta()).orElseThrow(() -> new RegraNegocioException("Id da consulta não existe"));;
        if (hierarquiaService.usuarioNaoTemPermissao(logado, pacienteRepository.findById(consulta.getPaciente().getId()).orElseThrow(() -> new RegraNegocioException("Id do paciente não existe")).getUsuario(), "ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Acesso negado: Apenas o paciente ou recepcionista podem agendar a consulta pra si mesmo.");
        }
        consulta.cancelar(dto.motivo());
        consultaRepository.save(consulta);
    }

    public Page<ListarConsultaDto> listar(Pageable paginacao, Usuario logado) {

        boolean ehPaciente = logado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
        if (ehPaciente) {
            return consultaRepository.findAllByPacienteUsuarioId(logado.getId(), paginacao).map(ListarConsultaDto::new);
        }
        else if (hierarquiaService.usuarioTemPermissao(logado, "ROLE_RECEPCIONISTA")) {
            return consultaRepository.findAll(paginacao).map(ListarConsultaDto::new);
        }
        else {
            throw new RegraNegocioException("Acesso negado: Apenas pacientes e funcionários autorizados podem listar consultas.");
        }
    }

    public Consulta detalhar(Long id, Usuario logado) {
        var consulta = consultaRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Id da consulta não existe"));
        if (hierarquiaService.usuarioNaoTemPermissao(logado, consulta.getPaciente().getUsuario(), "ROLE_RECEPCIONISTA")) {
            throw new RegraNegocioException("Acesso negado: Apenas o paciente participante ou funcionários autorizados podem detalhar esta consulta.");
        }
        return consulta;
    }
}
