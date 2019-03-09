package ch.start.hack.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import ch.start.hack.domain.enumeration.CupAction;

/**
 * A DTO for the History entity.
 */
public class HistoryDTO implements Serializable {

    private Long id;

    private CupAction action;

    private ZonedDateTime date;

    private Long kupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CupAction getAction() {
        return action;
    }

    public void setAction(CupAction action) {
        this.action = action;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getKupId() {
        return kupId;
    }

    public void setKupId(Long cupId) {
        this.kupId = cupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoryDTO historyDTO = (HistoryDTO) o;
        if (historyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", date='" + getDate() + "'" +
            ", kup=" + getKupId() +
            "}";
    }
}
