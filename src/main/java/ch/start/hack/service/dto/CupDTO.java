package ch.start.hack.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ch.start.hack.domain.History;
import ch.start.hack.domain.enumeration.CupStatus;

/**
 * A DTO for the Cup entity.
 */
public class CupDTO implements Serializable {

    private Long id;

    private String qrCode;

    private CupStatus status;

    private Long userCupId;

    private String userCupLogin;

    private Set<History> histories = new HashSet<>();

    public Set<History> getHistories() {
        return histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public CupStatus getStatus() {
        return status;
    }

    public void setStatus(CupStatus status) {
        this.status = status;
    }

    public Long getUserCupId() {
        return userCupId;
    }

    public void setUserCupId(Long userId) {
        this.userCupId = userId;
    }

    public String getUserCupLogin() {
        return userCupLogin;
    }

    public void setUserCupLogin(String userLogin) {
        this.userCupLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CupDTO cupDTO = (CupDTO) o;
        return Objects.equals(id, cupDTO.id) &&
            Objects.equals(qrCode, cupDTO.qrCode) &&
            status == cupDTO.status &&
            Objects.equals(userCupId, cupDTO.userCupId) &&
            Objects.equals(userCupLogin, cupDTO.userCupLogin) &&
            Objects.equals(histories, cupDTO.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qrCode, status, userCupId, userCupLogin, histories);
    }

    @Override
    public String toString() {
        return "CupDTO{" +
            "id=" + id +
            ", qrCode='" + qrCode + '\'' +
            ", status=" + status +
            ", userCupId=" + userCupId +
            ", userCupLogin='" + userCupLogin + '\'' +
            ", histories=" + histories +
            '}';
    }
}
