package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.infra.storage.entities.Image;
import org.springframework.content.commons.repository.ContentStore;

public interface ImageContentStore extends ContentStore<Image, String> {

}
