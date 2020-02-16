package eu.cipster.bizio.service;

import eu.cipster.bizio.domain.Contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Contract}.
 */
public interface ContractService {

    /**
     * Save a contract.
     *
     * @param contract the entity to save.
     * @return the persisted entity.
     */
    Contract save(Contract contract);

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Contract> findAll(Pageable pageable);

    /**
     * Get the "id" contract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Contract> findOne(Long id);

    /**
     * Delete the "id" contract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
