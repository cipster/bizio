package eu.cipster.bizio.service.impl;

import eu.cipster.bizio.service.LedgerItemService;
import eu.cipster.bizio.domain.LedgerItem;
import eu.cipster.bizio.repository.LedgerItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LedgerItem}.
 */
@Service
@Transactional
public class LedgerItemServiceImpl implements LedgerItemService {

    private final Logger log = LoggerFactory.getLogger(LedgerItemServiceImpl.class);

    private final LedgerItemRepository ledgerItemRepository;

    public LedgerItemServiceImpl(LedgerItemRepository ledgerItemRepository) {
        this.ledgerItemRepository = ledgerItemRepository;
    }

    /**
     * Save a ledgerItem.
     *
     * @param ledgerItem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LedgerItem save(LedgerItem ledgerItem) {
        log.debug("Request to save LedgerItem : {}", ledgerItem);
        return ledgerItemRepository.save(ledgerItem);
    }

    /**
     * Get all the ledgerItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LedgerItem> findAll(Pageable pageable) {
        log.debug("Request to get all LedgerItems");
        return ledgerItemRepository.findAll(pageable);
    }

    /**
     * Get one ledgerItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerItem> findOne(Long id) {
        log.debug("Request to get LedgerItem : {}", id);
        return ledgerItemRepository.findById(id);
    }

    /**
     * Delete the ledgerItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LedgerItem : {}", id);
        ledgerItemRepository.deleteById(id);
    }
}
