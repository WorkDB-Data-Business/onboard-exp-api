package br.com.harvest.onboardexperience.infra.scorm.services;

import br.com.harvest.onboardexperience.domain.exceptions.RegistrationNotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.infra.scorm.repository.ScormRegistrationRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class RegistrationService {

    @Autowired
    private ScormRegistrationRepository repository;

    public void disableRegister(@NonNull String registrationId){
        repository.delete(findById(registrationId));
    }

    private ScormRegistration findById(@NonNull String registrationId){
        return repository.findById(registrationId).orElseThrow(
                () -> new RegistrationNotFoundException(MessageFormat.format("SCORM Registration with ID {} was not found.", registrationId))
        );
    }


}
