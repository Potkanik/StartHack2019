package ch.start.hack.web.rest;

import ch.start.hack.domain.History;
import ch.start.hack.domain.enumeration.CupAction;
import ch.start.hack.service.CupService;
import ch.start.hack.service.HistoryService;
import ch.start.hack.service.dto.CupDTO;
import ch.start.hack.service.dto.HistoryDTO;
import ch.start.hack.service.mapper.CupMapper;
import ch.start.hack.service.mapper.HistoryMapper;
import ch.start.hack.web.rest.errors.BadRequestAlertException;
import ch.start.hack.web.rest.util.HeaderUtil;
import ch.start.hack.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cup.
 */
@RestController
@RequestMapping("/api")
public class CupResource {

    private final Logger log = LoggerFactory.getLogger(CupResource.class);

    private static final String ENTITY_NAME = "cup";

    private final CupService cupService;

    private final HistoryService historyService;

    private final CupMapper cupMapper;

    private final HistoryMapper historyMapper;

    public CupResource(CupService cupService, HistoryService historyService, CupMapper cupMapper, HistoryMapper historyMapper) {
        this.cupService = cupService;
        this.historyService = historyService;
        this.cupMapper = cupMapper;
        this.historyMapper = historyMapper;
    }

    /**
     * POST  /cups : Create a new cup.
     *
     * @param cupDTO the cupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cupDTO, or with status 400 (Bad Request) if the cup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cups")
    @Timed
    public ResponseEntity<CupDTO> createCup(@RequestBody CupDTO cupDTO) throws URISyntaxException {
        log.debug("REST request to save Cup : {}", cupDTO);
        if (cupDTO.getId() != null) {
            throw new BadRequestAlertException("A new cup cannot already have an ID", ENTITY_NAME, "idexists");
        }

        CupDTO result = cupService.save(cupDTO);

        History history = new History();
        history.setKup(cupMapper.toEntity(result));
        history.setDate(ZonedDateTime.now());
        history.setAction(CupAction.Created);

        HistoryDTO newHistory = historyService.save(historyMapper.toDto(history));

        History takenHistory = new History();
        takenHistory.setKup(cupMapper.toEntity(result));
        takenHistory.setDate(ZonedDateTime.now());
        takenHistory.setAction(CupAction.Taken);

        HistoryDTO newTakenHistory = historyService.save(historyMapper.toDto(takenHistory));

        result.getHistories().add(historyMapper.toEntity(newHistory));
        result.getHistories().add(historyMapper.toEntity(newTakenHistory));

        return ResponseEntity.created(new URI("/api/cups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cups : Updates an existing cup.
     *
     * @param cupDTO the cupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cupDTO,
     * or with status 400 (Bad Request) if the cupDTO is not valid,
     * or with status 500 (Internal Server Error) if the cupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cups")
    @Timed
    public ResponseEntity<CupDTO> updateCup(@RequestBody CupDTO cupDTO) throws URISyntaxException {
        log.debug("REST request to update Cup : {}", cupDTO);
        if (cupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CupDTO result = cupService.save(cupDTO);

        History history = new History();
        history.setDate(ZonedDateTime.now());
        history.setKup(cupMapper.toEntity(result));

        switch (cupDTO.getStatus()) {
            case InStore:
                history.setAction(CupAction.InStore);
                break;
            case InUse:
                history.setAction(CupAction.Taken);
                break;
            case Recycled:
                history.setAction(CupAction.Returned);
                break;
            case ReturnedByOther:
                history.setAction(CupAction.ReturnedByOther);
                break;
            case Lost:
                history.setAction(CupAction.Lost);
                break;
            default:
                log.debug("Something is wrong");
                break;
        }

        HistoryDTO newHistory = historyService.save(historyMapper.toDto(history));
        cupDTO.getHistories().add(historyMapper.toEntity(newHistory));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cupDTO.getId().toString()))
            .body(cupDTO);
    }

    /**
     * GET  /cups : get all the cups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cups in body
     */
    @GetMapping("/cups")
    @Timed
    public ResponseEntity<List<CupDTO>> getAllCups(Pageable pageable) {
        log.debug("REST request to get a page of Cups");
        Page<CupDTO> page = cupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cups");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /cups/:id : get the "id" cup.
     *
     * @param id the id of the cupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cups/{id}")
    @Timed
    public ResponseEntity<CupDTO> getCup(@PathVariable Long id) {
        log.debug("REST request to get Cup : {}", id);
        Optional<CupDTO> cupDTO = cupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cupDTO);
    }

    /**
     * DELETE  /cups/:id : delete the "id" cup.
     *
     * @param id the id of the cupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cups/{id}")
    @Timed
    public ResponseEntity<Void> deleteCup(@PathVariable Long id) {
        log.debug("REST request to delete Cup : {}", id);
        cupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
