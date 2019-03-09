package ch.start.hack.service;

import ch.start.hack.service.dto.CupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Cup.
 */
public interface CupService {

    /**
     * Save a cup.
     *
     * @param cupDTO the entity to save
     * @return the persisted entity
     */
    CupDTO save(CupDTO cupDTO);

    /**
     * Get all the cups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CupDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CupDTO> findOne(Long id);

    /**
     * Delete the "id" cup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
