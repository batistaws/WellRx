package batista.WellRx.infra.email;

import batista.WellRx.infra.exeption.RegraNegocioException;
import batista.WellRx.shared.database.model.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender enviadorEmail;

    @Value("${EMAIL_USERNAME}")
    private String EMAIL_ORIGEM;

    private static final String NOME_ENVIADOR = "Well Rx";

    public static final String URL_SITE = "http://localhost:8080";

    public EmailService(JavaMailSender enviadorEmail) {
        this.enviadorEmail = enviadorEmail;
    }


    public void enviarEmailVerificacao(Usuario usuario) {
        String assunto = "Aqui está seu link para verificar o email";
        String conteudo = gerarConteudoEmail("Olá [[name]],<br>"
                + "Por favor clique no link abaixo para verificar sua conta:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFICAR</a></h3>"
                + "Obrigado,<br>"
                + "Fórum Hub :).", usuario.getNomeCompleto(), URL_SITE + "/verificar-conta?token=" + usuario.getToken());

        enviarEmail(usuario.getEmail(), assunto, conteudo);
    }

    @Async
    private void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = enviadorEmail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);
        } catch(MessagingException | UnsupportedEncodingException e){
            throw new RegraNegocioException("Erro ao enviar email");
        }

        enviadorEmail.send(message);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }
}

