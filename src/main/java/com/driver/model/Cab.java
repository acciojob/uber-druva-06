package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "cab")
public class Cab{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int perKmRate;
    boolean available;

    public Cab() {
    }

    public Cab(int perKmRate, boolean available) {
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @JoinColumn
    @OneToOne
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}