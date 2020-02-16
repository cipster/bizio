package eu.cipster.bizio.service;

import eu.cipster.bizio.domain.InventoryItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link InventoryItem}.
 */
public interface InventoryItemService {

    /**
     * Save a inventoryItem.
     *
     * @param inventoryItem the entity to save.
     * @return the persisted entity.
     */
    InventoryItem save(InventoryItem inventoryItem);

    /**
     * Get all the inventoryItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InventoryItem> findAll(Pageable pageable);

    /**
     * Get the "id" inventoryItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InventoryItem> findOne(Long id);

    /**
     * Delete the "id" inventoryItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
