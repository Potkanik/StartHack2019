package ch.start.hack.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.start.hack.service.HistoryService;
import ch.start.hack.web.rest.errors.BadRequestAlertException;
import ch.start.hack.web.rest.util.HeaderUtil;
import ch.start.hack.web.rest.util.PaginationUtil;
import ch.start.hack.service.dto.HistoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing History.
 */
@RestController
@RequestMapping("/api")
public class HistoryResource {

    private final Logger log = LoggerFactory.getLogger(HistoryResource.class);

    private static final String ENTITY_NAME = "history";

    private final HistoryService historyService;

    public HistoryResource(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * POST  /histories : Create a new history.
     *
     * @param historyDTO the historyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historyDTO, or with status 400 (Bad Request) if the history has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/histories")
    @Timed
    public ResponseEntity<HistoryDTO> createHistory(@RequestBody HistoryDTO historyDTO) throws URISyntaxException {
        log.debug("REST request to save History : {}", historyDTO);
        if (historyDTO.getId() != null) {
            throw new BadRequestAlertException("A new history cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoryDTO result = historyService.save(historyDTO);
        return ResponseEntity.created(new URI("/api/histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /histories : Updates an existing history.
     *
     * @param historyDTO the historyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historyDTO,
     * or with status 400 (Bad Request) if the historyDTO is not valid,
     * or with status 500 (Internal Server Error) if the historyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/histories")
    @Timed
    public ResponseEntity<HistoryDTO> updateHistory(@RequestBody HistoryDTO historyDTO) throws URISyntaxException {
        log.debug("REST request to update History : {}", historyDTO);
        if (historyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HistoryDTO result = historyService.save(historyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /histories : get all the histories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of histories in body
     */
    @GetMapping("/histories")
    @Timed
    public ResponseEntity<List<HistoryDTO>> getAllHistories(Pageable pageable) {
        log.debug("REST request to get a page of Histories");
        Page<HistoryDTO> page = historyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /histories/:id : get the "id" history.
     *
     * @param id the id of the historyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/histories/{id}")
    @Timed
    public ResponseEntity<HistoryDTO> getHistory(@PathVariable Long id) {
        log.debug("REST request to get History : {}", id);
        Optional<HistoryDTO> historyDTO = historyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyDTO);
    }

    /**
     * DELETE  /histories/:id : delete the "id" history.
     *
     * @param id the id of the historyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        log.debug("REST request to delete History : {}", id);
        historyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
