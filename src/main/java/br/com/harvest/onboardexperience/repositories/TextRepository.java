package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.*;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;

@Repository
public interface TextRepository extends JpaRepository<Text,Long>, JpaSpecificationExecutor<Text> {


      static Specification<Text> byAuthorizedClients(@NonNull Client client) {
        return (text, cq, cb) -> {
            Join join = text.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }

    static Specification<Text> byAuthor(@NonNull User author) {
        return (text, cq, cb) -> cb.equal(text.get("author"), author);
    }

    static Specification<Text> byId(@NonNull Long id) {

          return (text, cq, cb) -> cb.equal(text.get("id"), id);
    }

}
