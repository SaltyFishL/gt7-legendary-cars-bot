package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 10:42
 */
public class Car {
    @JsonProperty("carid")
    private String carId;
    private String manufacturer;
    private String region;
    private String name;
    private long credits;
    private String state;
    @JsonProperty("estimatedays")
    private int estimateDays;
    @JsonProperty("new")
    private boolean isNew;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCredits() {
        return credits;
    }

    public void setCredits(long credits) {
        this.credits = credits;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getEstimateDays() {
        return estimateDays;
    }

    public void setEstimateDays(int estimateDays) {
        this.estimateDays = estimateDays;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
