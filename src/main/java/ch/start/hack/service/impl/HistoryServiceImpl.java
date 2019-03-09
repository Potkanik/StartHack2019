package ch.start.hack.service.impl;

import ch.start.hack.service.HistoryService;
import ch.start.hack.domain.History;
import ch.start.hack.repository.HistoryRepository;
import ch.start.hack.service.dto.HistoryDTO;
import ch.start.hack.service.mapper.HistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing History.
 */
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;

    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
    }

    /**
     * Save a history.
     *
     * @param historyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HistoryDTO save(HistoryDTO historyDTO) {
        log.debug("Request to save History : {}", historyDTO);

        History history = historyMapper.toEntity(historyDTO);
        history = historyRepository.save(history);
        return historyMapper.toDto(history);
    }

    /**
     * Get all the histories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Histories");
        return historyRepository.findAll(pageable)
            .map(historyMapper::toDto);
    }


    /**
     * Get one history by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryDTO> findOne(Long id) {
        log.debug("Request to get History : {}", id);
        return historyRepository.findById(id)
            .map(historyMapper::toDto);
    }

    /**
     * Delete the history by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete History : {}", id);
        historyRepository.deleteById(id);
    }
}
