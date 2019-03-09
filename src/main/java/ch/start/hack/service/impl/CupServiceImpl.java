package ch.start.hack.service.impl;

import ch.start.hack.repository.HistoryRepository;
import ch.start.hack.service.CupService;
import ch.start.hack.domain.Cup;
import ch.start.hack.repository.CupRepository;
import ch.start.hack.service.HistoryService;
import ch.start.hack.service.dto.CupDTO;
import ch.start.hack.service.mapper.CupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Cup.
 */
@Service
@Transactional
public class CupServiceImpl implements CupService {

    private final Logger log = LoggerFactory.getLogger(CupServiceImpl.class);

    private final CupRepository cupRepository;

    private final HistoryRepository historyRepository;

    private final CupMapper cupMapper;

    public CupServiceImpl(CupRepository cupRepository, HistoryRepository historyRepository, CupMapper cupMapper) {
        this.cupRepository = cupRepository;
        this.historyRepository = historyRepository;
        this.cupMapper = cupMapper;
    }

    /**
     * Save a cup.
     *
     * @param cupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CupDTO save(CupDTO cupDTO) {
        log.debug("Request to save Cup : {}", cupDTO);

        Cup cup = cupMapper.toEntity(cupDTO);
        cup.getHistories().forEach(historyRepository::save);
        cup = cupRepository.save(cup);
        return cupMapper.toDto(cup);
    }

    /**
     * Get all the cups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cups");
        return cupRepository.findAll(pageable)
            .map(cupMapper::toDto);
    }


    /**
     * Get one cup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CupDTO> findOne(Long id) {
        log.debug("Request to get Cup : {}", id);
        return cupRepository.findById(id)
            .map(cupMapper::toDto);
    }

    /**
     * Delete the cup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cup : {}", id);
        cupRepository.deleteById(id);
    }
}
