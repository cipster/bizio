package eu.cipster.bizio.service;

import eu.cipster.bizio.domain.LedgerItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link LedgerItem}.
 */
public interface LedgerItemService {

    /**
     * Save a ledgerItem.
     *
     * @param ledgerItem the entity to save.
     * @return the persisted entity.
     */
    LedgerItem save(LedgerItem ledgerItem);

    /**
     * Get all the ledgerItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LedgerItem> findAll(Pageable pageable);

    /**
     * Get the "id" ledgerItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LedgerItem> findOne(Long id);

    /**
     * Delete the "id" ledgerItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
