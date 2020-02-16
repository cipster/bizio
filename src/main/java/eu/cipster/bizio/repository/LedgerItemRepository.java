package eu.cipster.bizio.repository;

import eu.cipster.bizio.domain.LedgerItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LedgerItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LedgerItemRepository extends JpaRepository<LedgerItem, Long> {

}
