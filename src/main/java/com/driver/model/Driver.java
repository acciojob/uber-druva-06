package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
public class Driver{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String mobile;
    private String password;

    public Driver() {
    }

    public Driver(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    private Cab cab;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<TripBooking> tripBookingList = new ArrayList<>();

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }
}