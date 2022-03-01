package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import org.springframework.content.commons.repository.ContentStore;

public interface FileContentStore extends ContentStore<HarvestFile, String> {

}
