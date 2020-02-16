package eu.cipster.bizio.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cipster.bizio.web.rest.TestUtil;

public class LedgerItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LedgerItem.class);
        LedgerItem ledgerItem1 = new LedgerItem();
        ledgerItem1.setId(1L);
        LedgerItem ledgerItem2 = new LedgerItem();
        ledgerItem2.setId(ledgerItem1.getId());
        assertThat(ledgerItem1).isEqualTo(ledgerItem2);
        ledgerItem2.setId(2L);
        assertThat(ledgerItem1).isNotEqualTo(ledgerItem2);
        ledgerItem1.setId(null);
        assertThat(ledgerItem1).isNotEqualTo(ledgerItem2);
    }
}
