package br.com.harvest.onboardexperience.infra.storage.repositories;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.criteria.Join;
import java.util.Optional;


@RepositoryRestResource
public interface FileRepository extends JpaRepository<HarvestFile, Long>, JpaSpecificationExecutor<HarvestFile> {

    Optional<HarvestFile> findByContentPath(String contentPath);

    Boolean existsByNameAndAuthor_Client(String name, Client client);

    Optional<HarvestFile> findByIdAndAuthor(Long id, User author);

    static Specification<HarvestFile> byAuthorizedClients(@NonNull Client client) {
        return (file, cq, cb) -> {
            Join join = file.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }

    static Specification<HarvestFile> byIdAsString(@NonNull String id) {
        return (file, cq, cb) -> cb.equal(file.get("id").as(String.class), id);
    }

    static Specification<HarvestFile> byIsNotAsset() {
        return (file, cq, cb) -> cb.equal(file.get("isAsset"), false);
    }

    static Specification<HarvestFile> byCustomFilter(@NonNull String customFilter){
        return Specification.where(
                byIdAsString(customFilter))
                .or(byDescription(customFilter))
                .or(byName(customFilter))
                .or(byAuthor(customFilter))
                .or(byMimeType(customFilter));
    }

    static Specification<HarvestFile> byAuthor(@NonNull String criteria) {
        return (file, cq, cb) -> cb.or(
                cb.like(cb.lower(file.get("author").get("firstName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("lastName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("nickname")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("cpf")),  criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("email")), criteria.toLowerCase() + "%")
        );
    }

    static Specification<HarvestFile> byDescription(@NonNull String description) {
        return (file, cq, cb) -> cb.like(cb.lower(file.get("description")), "%" + description.toLowerCase() + "%");
    }

    static Specification<HarvestFile> byName(@NonNull String name) {
        return (file, cq, cb) -> cb.like(cb.lower(file.get("name")), "%" + name.toLowerCase() + "%");
    }

    static Specification<HarvestFile> byMimeType(@NonNull String mimeType) {
        return (file, cq, cb) -> cb.like(cb.lower(file.get("mimeType")), "%" + mimeType.toLowerCase() + "%");
    }

    static Specification<HarvestFile> byAuthor(@NonNull User author) {
        return (file, cq, cb) -> cb.equal(file.get("author"), author);
    }

    static Specification<HarvestFile> byClient(@NonNull Client client) {
        return (file, cq, cb) -> cb.equal(file.get("author").get("client"), client);
    }
}
