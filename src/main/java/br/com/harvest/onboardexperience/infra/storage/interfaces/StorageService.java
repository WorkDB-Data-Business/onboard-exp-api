package br.com.harvest.onboardexperience.infra.storage.interfaces;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface StorageService {

	void save(@NonNull UploadForm form, @NonNull String token);
	void validate(@NonNull UploadForm form);
	Page<?> findAll(@NonNull String token, Pageable pageable);
	void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token);
	void delete(@NonNull Long id, @NonNull String token);
	Optional<?> find(@NonNull Long id, @NonNull String token);
	void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull  List<Long> authorizedClients);


	static Client getAuthor(List<Client> authorizedClients){
		if(ObjectUtils.isNotEmpty(authorizedClients)){
			return authorizedClients.get(0);
		}
		return null;
	}

	static void validateAuthor(Client client, List<Client> authorizedClients){
		if(!client.equals(getAuthor(authorizedClients))){
			throw new GenericUploadException("Only the author can update a file.", "The client who request isn't the author" +
					" of the file");
		}
	}

	static List<Long> getIDFromClients(List<Client> clients){
		List<Long> ids = new ArrayList<>();
		if(ObjectUtils.isNotEmpty(clients)){
			ids = clients.stream().mapToLong(client -> client.getId()).boxed().collect(Collectors.toList());
		}
		return ids;
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
