package com.example.pureflow_group_project;

public class Reservoirs {
    String name;
    double lat;
    double lon;
    double lvl;

    public Reservoirs(String name, double lat, double lon, double lvl) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.lvl = lvl;
    }

    // Blank Constructor Needed for Firebase Database
    public Reservoirs() {
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLvl() {
        return lvl;
    }

}
