package pojo;

import java.util.List;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 10:49
 */
public class LegendDealerShip {
    private String date;
    private List<Car> cars;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

}
