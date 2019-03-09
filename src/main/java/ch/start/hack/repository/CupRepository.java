package ch.start.hack.repository;

import ch.start.hack.domain.Cup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CupRepository extends JpaRepository<Cup, Long> {

    @Query("select cup from Cup cup where cup.userCup.login = ?#{principal.username}")
    List<Cup> findByUserCupIsCurrentUser();

    Optional<Cup> findCupByQrCode(String qrCode);

}
