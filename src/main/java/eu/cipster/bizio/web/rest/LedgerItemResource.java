package eu.cipster.bizio.web.rest;

import eu.cipster.bizio.domain.LedgerItem;
import eu.cipster.bizio.service.LedgerItemService;
import eu.cipster.bizio.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link eu.cipster.bizio.domain.LedgerItem}.
 */
@RestController
@RequestMapping("/api")
public class LedgerItemResource {

    private final Logger log = LoggerFactory.getLogger(LedgerItemResource.class);

    private static final String ENTITY_NAME = "ledgerItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LedgerItemService ledgerItemService;

    public LedgerItemResource(LedgerItemService ledgerItemService) {
        this.ledgerItemService = ledgerItemService;
    }

    /**
     * {@code POST  /ledger-items} : Create a new ledgerItem.
     *
     * @param ledgerItem the ledgerItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ledgerItem, or with status {@code 400 (Bad Request)} if the ledgerItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ledger-items")
    public ResponseEntity<LedgerItem> createLedgerItem(@Valid @RequestBody LedgerItem ledgerItem) throws URISyntaxException {
        log.debug("REST request to save LedgerItem : {}", ledgerItem);
        if (ledgerItem.getId() != null) {
            throw new BadRequestAlertException("A new ledgerItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LedgerItem result = ledgerItemService.save(ledgerItem);
        return ResponseEntity.created(new URI("/api/ledger-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ledger-items} : Updates an existing ledgerItem.
     *
     * @param ledgerItem the ledgerItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerItem,
     * or with status {@code 400 (Bad Request)} if the ledgerItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ledgerItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ledger-items")
    public ResponseEntity<LedgerItem> updateLedgerItem(@Valid @RequestBody LedgerItem ledgerItem) throws URISyntaxException {
        log.debug("REST request to update LedgerItem : {}", ledgerItem);
        if (ledgerItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LedgerItem result = ledgerItemService.save(ledgerItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ledgerItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ledger-items} : get all the ledgerItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ledgerItems in body.
     */
    @GetMapping("/ledger-items")
    public ResponseEntity<List<LedgerItem>> getAllLedgerItems(Pageable pageable) {
        log.debug("REST request to get a page of LedgerItems");
        Page<LedgerItem> page = ledgerItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ledger-items/:id} : get the "id" ledgerItem.
     *
     * @param id the id of the ledgerItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ledgerItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ledger-items/{id}")
    public ResponseEntity<LedgerItem> getLedgerItem(@PathVariable Long id) {
        log.debug("REST request to get LedgerItem : {}", id);
        Optional<LedgerItem> ledgerItem = ledgerItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ledgerItem);
    }

    /**
     * {@code DELETE  /ledger-items/:id} : delete the "id" ledgerItem.
     *
     * @param id the id of the ledgerItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ledger-items/{id}")
    public ResponseEntity<Void> deleteLedgerItem(@PathVariable Long id) {
        log.debug("REST request to delete LedgerItem : {}", id);
        ledgerItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
