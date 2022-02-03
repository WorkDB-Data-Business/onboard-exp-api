package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long>, JpaSpecificationExecutor<Trail> {

    Boolean existsByNameAndClient(String name, Client client);

    Optional<Trail> findByIdAndClient(Long id, Client client);

    static Specification<Trail> byAuthor(@NonNull User author) {
        return (trail, cq, cb) -> cb.equal(trail.get("author"), author);
    }

    static Specification<Trail> byDescription(@NonNull String description) {
        return (trail, cq, cb) -> cb.like(cb.lower(trail.get("description")), "%" + description.toLowerCase() + "%");
    }

    static Specification<Trail> byName(@NonNull String name) {
        return (trail, cq, cb) -> cb.like(cb.lower(trail.get("name")), "%" + name.toLowerCase() + "%");
    }

    static Specification<Trail> byIsActive(@NonNull Boolean isActive) {
        return (trail, cq, cb) -> cb.equal(trail.get("name"), isActive);
    }

    static Specification<Trail> byId(@NonNull Long id) {
        return (trail, cq, cb) -> cb.equal(trail.get("id"), id);
    }

    static Specification<Trail> byCoin(@NonNull Coin coin) {
        return (trail, cq, cb) -> cb.equal(trail.get("coin"), coin);
    }

    static Specification<Trail> byAuthor(@NonNull String criteria) {
        return (trail, cq, cb) -> cb.or(
                cb.like(cb.lower(trail.get("author").get("firstName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(trail.get("author").get("lastName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(trail.get("author").get("nickname")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(trail.get("author").get("cpf")),  criteria.toLowerCase() + "%"),
                cb.like(cb.lower(trail.get("author").get("email")), criteria.toLowerCase() + "%")
        );
    }

    static Specification<Trail> byCoin(@NonNull String criteria) {
        return (trail, cq, cb) -> cb.or(
                cb.like(cb.lower(trail.get("coin").get("name")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(trail.get("author").get("id").as(String.class)),criteria.toLowerCase() + "%")
        );
    }

    static Specification<Trail> byEndUser(@NonNull User user) {
        return (trail, cq, cb) -> {
            Join<Trail, Group> joinGroups = trail.join("groups", JoinType.INNER);
            Join<Group, CompanyRole> joinCompanyRolesGroup = joinGroups.join("companyRoles", JoinType.LEFT);
            Join<Group, User> joinUsersGroup = joinGroups.join("users", JoinType.LEFT);

            cq.distinct(true);

            return cb.or(
                    cb.equal(joinUsersGroup, user),
                    cb.equal(joinCompanyRolesGroup, user.getCompanyRole())
            );
        };
    }

    static Specification<Trail> byEndUserOrAuthor(@NonNull User user){
        return Specification.where(byAuthor(user)).or(byEndUser(user));
    }

}
