package eu.cipster.bizio.service.impl;

import eu.cipster.bizio.service.InventoryItemService;
import eu.cipster.bizio.domain.InventoryItem;
import eu.cipster.bizio.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InventoryItem}.
 */
@Service
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    private final Logger log = LoggerFactory.getLogger(InventoryItemServiceImpl.class);

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /**
     * Save a inventoryItem.
     *
     * @param inventoryItem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InventoryItem save(InventoryItem inventoryItem) {
        log.debug("Request to save InventoryItem : {}", inventoryItem);
        return inventoryItemRepository.save(inventoryItem);
    }

    /**
     * Get all the inventoryItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryItem> findAll(Pageable pageable) {
        log.debug("Request to get all InventoryItems");
        return inventoryItemRepository.findAll(pageable);
    }

    /**
     * Get one inventoryItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItem> findOne(Long id) {
        log.debug("Request to get InventoryItem : {}", id);
        return inventoryItemRepository.findById(id);
    }

    /**
     * Delete the inventoryItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InventoryItem : {}", id);
        inventoryItemRepository.deleteById(id);
    }
}
