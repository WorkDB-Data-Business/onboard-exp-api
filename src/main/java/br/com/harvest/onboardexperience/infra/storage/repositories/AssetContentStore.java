package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.infra.storage.entities.Asset;
import org.springframework.content.commons.repository.ContentStore;

public interface AssetContentStore extends ContentStore<Asset, String> {

}
