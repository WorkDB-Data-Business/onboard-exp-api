package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.entities.UserCoin;
import br.com.harvest.onboardexperience.domain.entities.UserCoinKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, UserCoinKey> {

    List<UserCoin> findByUser(User user);

}
