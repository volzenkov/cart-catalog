package com.aqua.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by kama3 on 15.07.2015.
 */
@Embeddable
public class Dimensions implements Serializable {

    private double length;
    private double width;
    private double height;

    private double weight;

    public Dimensions() {
    }

    public Dimensions(double length, double width, double height, double weight) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
