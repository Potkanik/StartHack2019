package ch.start.hack.domain.pojo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FinalViewData implements Serializable {

    public Integer id;
    public String name;
    public Integer money;
    public Integer countOfCups;
    public Integer countOfReturnedCups;
    public Integer countOfReturnedCupsByOthers;
    public Long[] chartValues;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getCountOfCups() {
        return countOfCups;
    }

    public void setCountOfCups(Integer countOfCups) {
        this.countOfCups = countOfCups;
    }

    public Integer getCountOfReturnedCups() {
        return countOfReturnedCups;
    }

    public void setCountOfReturnedCups(Integer countOfReturnedCups) {
        this.countOfReturnedCups = countOfReturnedCups;
    }

    public Integer getCountOfReturnedCupsByOthers() {
        return countOfReturnedCupsByOthers;
    }

    public void setCountOfReturnedCupsByOthers(Integer countOfReturnedCupsByOthers) {
        this.countOfReturnedCupsByOthers = countOfReturnedCupsByOthers;
    }

    public Long[] getChartValues() {
        return chartValues;
    }

    public void setChartValues(Long[] chartValues) {
        this.chartValues = chartValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalViewData that = (FinalViewData) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(money, that.money) &&
            Objects.equals(countOfCups, that.countOfCups) &&
            Objects.equals(countOfReturnedCups, that.countOfReturnedCups) &&
            Objects.equals(countOfReturnedCupsByOthers, that.countOfReturnedCupsByOthers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, money, countOfCups, countOfReturnedCups, countOfReturnedCupsByOthers);
    }

    @Override
    public String toString() {
        return "FinalViewData{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", money=" + money +
            ", countOfCups=" + countOfCups +
            ", countOfReturnedCups=" + countOfReturnedCups +
            ", countOfReturnedCupsByOthers=" + countOfReturnedCupsByOthers +
            ", chartValues=" + Arrays.toString(chartValues) +
            '}';
    }
}
