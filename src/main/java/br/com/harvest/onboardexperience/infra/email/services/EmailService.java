package br.com.harvest.onboardexperience.infra.email.services;

import br.com.harvest.onboardexperience.configurations.environment.EmailEnvironment;
import br.com.harvest.onboardexperience.infra.email.dtos.EmailMessage;
import br.com.harvest.onboardexperience.infra.email.interfaces.EmailSender;
import com.sun.mail.smtp.SMTPTransport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class EmailService implements EmailSender {

    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private EmailEnvironment emailEnvironment;

    @Autowired
    private JavaMailSender sender;

    private final String FROM_HARVEST = "Equipe Harvest <noreply@harvest.com.br>";
    
    @Override
    public void send(EmailMessage message) throws Exception {

        try {
            var body = proccessModel(message);
            var mimeMessage = configureSender();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom(FROM_HARVEST);
            mimeMessageHelper.setTo(message.getReceivers().toArray(new String[0]));
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(body, true);

            this.sendEmail(mimeMessage);
        } catch (Exception e) {
            log.error("An error occurs while sending the email.", e);
            throw new Exception(e);
        }
    }

    @Override
    public void send(EmailMessage message, ByteArrayResource attach) throws Exception {

        try {
            var body = proccessModel(message);
            var mimeMessage = configureSender();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(FROM_HARVEST);
            mimeMessageHelper.setTo(message.getReceivers().toArray(new String[0]));
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(body, true);

            mimeMessageHelper.addAttachment(attach.getFilename(), attach);

            this.sendEmail(mimeMessage);
        } catch (Exception e) {
            log.error("An error occurs while sending the email.", e);
            throw new Exception(e);
        }
    }

    @Override
    public void send(EmailMessage message, List<ByteArrayResource> attachments) throws Exception {
        try {
            var body = proccessModel(message);
            var mimeMessage = configureSender();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(FROM_HARVEST);
            mimeMessageHelper.setTo(message.getReceivers().toArray(new String[0]));
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(body, true);

            for (ByteArrayResource byteArrayResource : attachments) {
                mimeMessageHelper.addAttachment(byteArrayResource.getFilename(), byteArrayResource);
            }

            this.sendEmail(mimeMessage);
        } catch (Exception e) {
            log.error("An error occurs while sending the email.", e);
            throw new Exception(e);
        }
    }

    private void sendEmail(MimeMessage mimeMessage) throws Exception {
        try {
            SMTPTransport transport = (SMTPTransport) mimeMessage.getSession().getTransport("smtp");
            transport.connect(emailEnvironment.getHost(), emailEnvironment.getUsername(), emailEnvironment.getPassword());

            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        } catch (Exception e){
            log.error("An error occurs while sending the email.", e);
            throw new Exception(e);
        }
    }


    public String proccessModel(EmailMessage mensagem) throws Exception {
        Template template = freemarkerConfiguration.getTemplate(mensagem.getModel());
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariables());
    }

    private MimeMessage configureSender() {
        this.sender = new JavaMailSenderImpl();
        Session session = Session.getDefaultInstance(this.getProperties());
        session.getProperties().setProperty("mail.mime.address.strict", "false");
        return new MimeMessage(session);
    }

    private Properties getProperties() {

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", emailEnvironment.getPort());
        props.put("mail.smtp.auth", emailEnvironment.getSmtpAuth());
        props.put("mail.smtp.auth.login.disable", "false");
        props.put("mail.host", emailEnvironment.getHost());
        props.put("mail.username", emailEnvironment.getUsername());
        props.put("mail.password", emailEnvironment.getPassword());

        if (emailEnvironment.getSmtpSslEnable()) {
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        }

        return props;
    }
}
