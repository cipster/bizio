package eu.cipster.bizio.web.rest;

import eu.cipster.bizio.domain.InventoryItem;
import eu.cipster.bizio.service.InventoryItemService;
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
 * REST controller for managing {@link eu.cipster.bizio.domain.InventoryItem}.
 */
@RestController
@RequestMapping("/api")
public class InventoryItemResource {

    private final Logger log = LoggerFactory.getLogger(InventoryItemResource.class);

    private static final String ENTITY_NAME = "inventoryItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoryItemService inventoryItemService;

    public InventoryItemResource(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    /**
     * {@code POST  /inventory-items} : Create a new inventoryItem.
     *
     * @param inventoryItem the inventoryItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoryItem, or with status {@code 400 (Bad Request)} if the inventoryItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventory-items")
    public ResponseEntity<InventoryItem> createInventoryItem(@Valid @RequestBody InventoryItem inventoryItem) throws URISyntaxException {
        log.debug("REST request to save InventoryItem : {}", inventoryItem);
        if (inventoryItem.getId() != null) {
            throw new BadRequestAlertException("A new inventoryItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InventoryItem result = inventoryItemService.save(inventoryItem);
        return ResponseEntity.created(new URI("/api/inventory-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inventory-items} : Updates an existing inventoryItem.
     *
     * @param inventoryItem the inventoryItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItem,
     * or with status {@code 400 (Bad Request)} if the inventoryItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventory-items")
    public ResponseEntity<InventoryItem> updateInventoryItem(@Valid @RequestBody InventoryItem inventoryItem) throws URISyntaxException {
        log.debug("REST request to update InventoryItem : {}", inventoryItem);
        if (inventoryItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InventoryItem result = inventoryItemService.save(inventoryItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inventory-items} : get all the inventoryItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoryItems in body.
     */
    @GetMapping("/inventory-items")
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems(Pageable pageable) {
        log.debug("REST request to get a page of InventoryItems");
        Page<InventoryItem> page = inventoryItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventory-items/:id} : get the "id" inventoryItem.
     *
     * @param id the id of the inventoryItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoryItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventory-items/{id}")
    public ResponseEntity<InventoryItem> getInventoryItem(@PathVariable Long id) {
        log.debug("REST request to get InventoryItem : {}", id);
        Optional<InventoryItem> inventoryItem = inventoryItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryItem);
    }

    /**
     * {@code DELETE  /inventory-items/:id} : delete the "id" inventoryItem.
     *
     * @param id the id of the inventoryItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventory-items/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        log.debug("REST request to delete InventoryItem : {}", id);
        inventoryItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
