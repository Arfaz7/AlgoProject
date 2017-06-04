package GraphPackage;

import java.util.ArrayList;

/**
 * Created by arfaz on 15/05/2017.
 */
public class Station {

    private ArrayList<Integer> idList;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private ArrayList<Railway> railwayList;
    private ArrayList<Station> transferStations;

    public Station() {
    }

    public Station(Integer id, String name, String address, double latitude, double longitude) {
        this.idList = new ArrayList<>();
        this.idList.add(id);
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.railwayList = new ArrayList<>();
        this.transferStations = new ArrayList<>();
    }

    public ArrayList<Integer> getIdList() { return idList; }

    public void setIdList(ArrayList<Integer> idList) {
        this.idList = idList;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public ArrayList<Railway> getRailwayList() {
        return railwayList;
    }

    public void setRailwayList(ArrayList<Railway> railwayList) {
        this.railwayList = railwayList;
    }

    public ArrayList<Station> getTransferStations() {
        return transferStations;
    }

    public void setTransferStations(ArrayList<Station> transferStations) {
        this.transferStations = transferStations;
    }

}
