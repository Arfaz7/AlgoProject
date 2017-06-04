package GraphPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by arfaz on 15/05/2017.
 */
public class Subway extends AbstractGraph{

    private String name;

    public Subway(String name, boolean weighted) {
        super(weighted);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Station getStationById(Integer id) {
        Station currentStation = null;

        for(Map.Entry<String, ArrayList<Station>> entry : this.getStationList().entrySet()) {

            if(entry.getValue().get(0).getIdList().contains(id)) {
                currentStation = entry.getValue().get(0);
            }
        }

        return currentStation;
    }

    public void generateStationList(ArrayList<Station> stations) {

        for (Station s : stations) {
            this.getStationList().put(s.getName(), new ArrayList<>(Arrays.asList(s)));
        }
    }
}
