package CustomCollectionPackage;

import GraphPackage.Station;

import java.util.ArrayList;

/**
 * Created by arfaz on 28/05/2017.
 */
public class CustomQueue extends CustomCollection {

    public CustomQueue() {
        super();
    }

    @Override
    public ArrayList<Station> get() {
        ArrayList<Station> tmpStation = this.getStationList().get(0);
        this.getStationList().remove(0);

        return tmpStation;
    }
}
