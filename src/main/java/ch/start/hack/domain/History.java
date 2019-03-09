package ch.start.hack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import ch.start.hack.domain.enumeration.CupAction;

/**
 * A History.
 */
@Entity
@Table(name = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private CupAction action;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @ManyToOne
    @JsonIgnore
    private Cup kup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CupAction getAction() {
        return action;
    }

    public History action(CupAction action) {
        this.action = action;
        return this;
    }

    public void setAction(CupAction action) {
        this.action = action;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public History date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Cup getKup() {
        return kup;
    }

    public History kup(Cup cup) {
        this.kup = cup;
        return this;
    }

    public void setKup(Cup cup) {
        this.kup = cup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        History history = (History) o;
        if (history.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), history.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "History{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
