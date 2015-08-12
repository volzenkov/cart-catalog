package com.aqua.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by kama3 on 15.07.2015.
 */
@Embeddable
public class Price implements Serializable {

    private double basePrice;
    private String currency;
    private double tax;

    public Price() {
    }

    public Price(double basePrice, String currency, double tax) {
        this.basePrice = basePrice;
        this.currency = currency;
        this.tax = tax;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}
