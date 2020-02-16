package eu.cipster.bizio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import eu.cipster.bizio.domain.enumeration.LedgerType;

/**
 * A LedgerItem.
 */
@Entity
@Table(name = "ledger_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LedgerItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "document", nullable = false)
    private String document;

    @NotNull
    @Column(name = "explanation", nullable = false)
    private String explanation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LedgerType type;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToOne
    @JsonIgnoreProperties("ledgerItems")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LedgerItem date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDocument() {
        return document;
    }

    public LedgerItem document(String document) {
        this.document = document;
        return this;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getExplanation() {
        return explanation;
    }

    public LedgerItem explanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LedgerType getType() {
        return type;
    }

    public LedgerItem type(LedgerType type) {
        this.type = type;
        return this;
    }

    public void setType(LedgerType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LedgerItem value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getYear() {
        return year;
    }

    public LedgerItem year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Client getClient() {
        return client;
    }

    public LedgerItem client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LedgerItem)) {
            return false;
        }
        return id != null && id.equals(((LedgerItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LedgerItem{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", document='" + getDocument() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            ", year=" + getYear() +
            "}";
    }
}
