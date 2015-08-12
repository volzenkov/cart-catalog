package com.aqua.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by kama3 on 15.07.2015.
 */
@Embeddable
public class Stock implements Serializable {

    private double quantity;
    private double minimumQuantity;
    private String location;
    private String status;

    public Stock() {
    }

    public Stock(double quantity, double minimumQuantity, String location, String status) {
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.location = location;
        this.status = status;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(double minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
