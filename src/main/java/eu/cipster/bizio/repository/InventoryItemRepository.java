package eu.cipster.bizio.repository;

import eu.cipster.bizio.domain.InventoryItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InventoryItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

}
