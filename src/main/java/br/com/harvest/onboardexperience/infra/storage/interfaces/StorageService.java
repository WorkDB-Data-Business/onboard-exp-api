package br.com.harvest.onboardexperience.infra.storage.interfaces;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.ForbiddenAccess;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface StorageService {

	void save(@NonNull UploadForm form, @NonNull String token);
	void validate(@NonNull UploadForm form);
	Page<?> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable);
	void update(@NonNull String id, @NonNull UploadForm form, @NonNull String token) throws Exception;
	void delete(@NonNull String id, @NonNull String token) throws Exception;
	Optional<?> find(@NonNull String id, @NonNull String token) throws Exception;
	void updateAuthorizedClients(@NonNull String id, @NonNull String token, @NonNull  List<Long> authorizedClients) throws Exception;

	static void validateAuthor(Object object, Class<?> classToValidate, User user, String messageError){
		User author = (User) GenericUtils.executeMethodFromGenericClass(classToValidate, "getAuthor", Optional.of(object));
		if(!user.equals(author)){
			throw new ForbiddenAccess(messageError);
		}
	}

	static String encodeFileToBase64(InputStream inputStream){
		try {
			return Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
