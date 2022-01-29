package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long>, JpaSpecificationExecutor<Link> {

    Page<Link> findAllByAuthorizedClients(Client client, Pageable pageable);

    Optional<Link> findByIdAndAuthorizedClients(Long id, Client client);

    Optional<Link> findByIdAndAuthor(Long id, User author);

    static Specification<Link> byAuthor(@NonNull String criteria) {
        return (file, cq, cb) -> cb.or(
                cb.like(cb.lower(file.get("author").get("firstName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("lastName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("nickname")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("cpf")),  criteria.toLowerCase() + "%"),
                cb.like(cb.lower(file.get("author").get("email")), criteria.toLowerCase() + "%")
        );
    }

    static Specification<Link> byAuthor(@NonNull User author) {
        return (link, cq, cb) -> cb.equal(link.get("author"), author);
    }

    static Specification<Link> byAuthorizedClients(@NonNull Client client) {
        return (link, cq, cb) -> {
            Join join = link.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }

    static Specification<Link> byCustomFilter(@NonNull String customFilter){
        return Specification.where(
                        byIdAsString(customFilter))
                .or(byDescription(customFilter))
                .or(byLink(customFilter))
                .or(byAuthor(customFilter))
                .or(byContentType(customFilter));
    }

    static Specification<Link> byDescription(@NonNull String description) {
        return (link, cq, cb) -> cb.like(cb.lower(link.get("description")), "%" + description.toLowerCase() + "%");
    }

    static Specification<Link> byContentType(@NonNull String contentType) {
        return (link, cq, cb) -> cb.like(cb.lower(link.get("contentType")), "%" + contentType.toLowerCase() + "%");
    }

    static Specification<Link> byLink(@NonNull String linkDescription) {
        return (link, cq, cb) -> cb.like(cb.lower(link.get("link")), "%" + linkDescription.toLowerCase() + "%");
    }

    static Specification<Link> byIdAsString(@NonNull String id) {
        return (link, cq, cb) -> cb.equal(link.get("id").as(String.class), id);
    }
}
