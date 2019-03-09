package ch.start.hack.repository;

import ch.start.hack.domain.Cup;
import ch.start.hack.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the History entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findHistoryByKup(Cup cup);
}
