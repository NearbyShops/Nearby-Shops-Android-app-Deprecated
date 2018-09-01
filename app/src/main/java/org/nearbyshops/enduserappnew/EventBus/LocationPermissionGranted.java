package org.nearbyshops.enduserappnew.EventBus;

public class LocationPermissionGranted {

    public LocationPermissionGranted(int abcd, double aofija) {
        this.abcd = abcd;
        this.aofija = aofija;
    }

    private int abcd;
    private double aofija;


    /// getter and setters

    public int getAbcd() {
        return abcd;
    }

    public void setAbcd(int abcd) {
        this.abcd = abcd;
    }

    public double getAofija() {
        return aofija;
    }

    public void setAofija(double aofija) {
        this.aofija = aofija;
    }
}
