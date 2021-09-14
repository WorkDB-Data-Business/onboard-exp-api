package br.com.harvest.onboardexperience.infra.email.interfaces;

import br.com.harvest.onboardexperience.infra.email.dtos.EmailMessage;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public interface EmailSender {

    void send(EmailMessage message) throws Exception;

    void send(EmailMessage message, ByteArrayResource attachment) throws Exception;

    void send(EmailMessage message, List<ByteArrayResource> attachments) throws Exception;

}
