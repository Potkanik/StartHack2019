package ch.start.hack.domain.pojo;

import java.util.Objects;

public class CupRequest {

    private String userLogin;

    private String cupHash;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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
        return Objects.equals(userLogin, that.userLogin) &&
            Objects.equals(cupHash, that.cupHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin, cupHash);
    }

    @Override
    public String toString() {
        return "CupRequest{" +
            "userLogin='" + userLogin + '\'' +
            ", cupHash='" + cupHash + '\'' +
            '}';
    }
}
