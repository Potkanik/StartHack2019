package ch.start.hack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ch.start.hack.domain.enumeration.CupStatus;

/**
 * A Cup.
 */
@Entity
@Table(name = "cup")
public class Cup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "qr_code")
    private String qrCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CupStatus status;

    @OneToMany(mappedBy = "kup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<History> histories = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    private User userCup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Cup qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public CupStatus getStatus() {
        return status;
    }

    public Cup status(CupStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CupStatus status) {
        this.status = status;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public Cup histories(Set<History> histories) {
        this.histories = histories;
        return this;
    }

    public Cup addHistory(History history) {
        this.histories.add(history);
        history.setKup(this);
        return this;
    }

    public Cup removeHistory(History history) {
        this.histories.remove(history);
        history.setKup(null);
        return this;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public User getUserCup() {
        return userCup;
    }

    public Cup userCup(User user) {
        this.userCup = user;
        return this;
    }

    public void setUserCup(User user) {
        this.userCup = user;
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
        Cup cup = (Cup) o;
        if (cup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cup{" +
            "id=" + getId() +
            ", qrCode='" + getQrCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
