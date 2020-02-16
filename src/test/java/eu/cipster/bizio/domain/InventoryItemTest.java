package eu.cipster.bizio.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cipster.bizio.web.rest.TestUtil;

public class InventoryItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItem.class);
        InventoryItem inventoryItem1 = new InventoryItem();
        inventoryItem1.setId(1L);
        InventoryItem inventoryItem2 = new InventoryItem();
        inventoryItem2.setId(inventoryItem1.getId());
        assertThat(inventoryItem1).isEqualTo(inventoryItem2);
        inventoryItem2.setId(2L);
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);
        inventoryItem1.setId(null);
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);
    }
}
