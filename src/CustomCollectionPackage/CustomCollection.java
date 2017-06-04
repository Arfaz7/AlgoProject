package CustomCollectionPackage;

import GraphPackage.Station;

import java.util.ArrayList;

/**
 * Created by arfaz on 28/05/2017.
 */
public abstract class CustomCollection {

    private ArrayList<ArrayList<Station>> stationList;

    public CustomCollection() {
        this.stationList = new ArrayList<>();
    }

    public void add(ArrayList<Station> stationList) {
        this.stationList.add(stationList);
    }

    public abstract ArrayList<Station> get();

    public boolean isEmpty() {
        return this.stationList.isEmpty();
    }

    public ArrayList<ArrayList<Station>> getStationList() {
        return stationList;
    }

}
