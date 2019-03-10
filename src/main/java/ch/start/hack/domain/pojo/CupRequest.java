package ch.start.hack.domain.pojo;

import java.util.Objects;

public class CupRequest {

    private String userHash;

    private String cupHash;

    public String getCardHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getCupHash() {
        return cupHash;
    }

    public void setCupHash(String cupHash) {
        this.cupHash = cupHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CupRequest that = (CupRequest) o;
        return Objects.equals(userHash, that.userHash) &&
            Objects.equals(cupHash, that.cupHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userHash, cupHash);
    }

    @Override
    public String toString() {
        return "CupRequest{" +
            "userHash='" + userHash + '\'' +
            ", cupHash='" + cupHash + '\'' +
            '}';
    }
}
