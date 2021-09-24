package br.com.harvest.onboardexperience.infra.storage.interfaces;

import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorageService {

	void save(@NonNull UploadForm form, String token);
	void validate(@NonNull UploadForm form);
	Page<?> findAll(@NonNull String token, Pageable pageable);

}
