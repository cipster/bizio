package eu.cipster.bizio.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LedgerItem> ledgerItems = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contract> contracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LedgerItem> getLedgerItems() {
        return ledgerItems;
    }

    public Client ledgerItems(Set<LedgerItem> ledgerItems) {
        this.ledgerItems = ledgerItems;
        return this;
    }

    public Client addLedgerItem(LedgerItem ledgerItem) {
        this.ledgerItems.add(ledgerItem);
        ledgerItem.setClient(this);
        return this;
    }

    public Client removeLedgerItem(LedgerItem ledgerItem) {
        this.ledgerItems.remove(ledgerItem);
        ledgerItem.setClient(null);
        return this;
    }

    public void setLedgerItems(Set<LedgerItem> ledgerItems) {
        this.ledgerItems = ledgerItems;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public Client contracts(Set<Contract> contracts) {
        this.contracts = contracts;
        return this;
    }

    public Client addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setClient(this);
        return this;
    }

    public Client removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setClient(null);
        return this;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
