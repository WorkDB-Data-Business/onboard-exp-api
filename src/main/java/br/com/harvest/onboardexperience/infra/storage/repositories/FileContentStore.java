package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.infra.storage.entities.File;
import org.springframework.content.commons.repository.ContentStore;

public interface FileContentStore extends ContentStore<File, String> {

}
