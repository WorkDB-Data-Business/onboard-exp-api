package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.PasswordResetToken;
import br.com.harvest.onboardexperience.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findAllByUserAndIsExpired(User user, Boolean isExpired);

}
