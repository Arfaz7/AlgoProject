package GraphPackage;

import CustomCollectionPackage.CustomCollection;
import CustomCollectionPackage.CustomQueue;
import ShortestPathPackage.BFSShortestPath;
import ShortestPathPackage.DjikstraSP;
import ShortestPathPackage.ShortestPath;
import com.sun.org.glassfish.external.statistics.StringStatistic;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by arfaz on 28/05/2017.
 */
public abstract class AbstractGraph {

    private HashMap<String, ArrayList<Station>> stationList;
    private ArrayList<Railway> railwayList;
    private boolean isWeighted;

    public AbstractGraph(boolean isWeighted) {
        this.stationList = new HashMap<>();
        this.railwayList = new ArrayList<>();
        this.isWeighted = isWeighted;
    }

    public HashMap<String, ArrayList<Station>> getStationList() {
        return stationList;
    }

    public void setStationList(HashMap<String, ArrayList<Station>> stationList) {
        this.stationList = stationList;
    }

    public ArrayList<Railway> getRailwayList() {
        return railwayList;
    }

    public void setRailwayList(ArrayList<Railway> railwayList) {
        this.railwayList = railwayList;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public void setWeighted(boolean weighted) {
        isWeighted = weighted;
    }

    public void resetBetweenness() {
        for (Railway r : this.railwayList) {
            r.setBetweenness(0);
        }
    }
    public double getRailwayDistance(String fromName, String toName) {
        double distance = 0.;

        for(Railway r : this.getRailwayList()) {
            if(r.getOrigin().getName().equals(fromName) && r.getDestination().getName().equals(toName)) {
                distance = r.getDistance();
            }
        }

        return distance;
    }

    public ArrayList<Station> stationExists(Station s) {
        return this.getStationList().get(s.getName());

    }

    public Railway findRailway(String from, String to) {
        Railway currentRailway = null;
        int i = 0;

        while(currentRailway == null && i < this.getRailwayList().size()) {
            if(this.getRailwayList().get(i).getOrigin().getName().equals(from) && this.getRailwayList().get(i).getDestination().getName().equals(to)) {
                currentRailway = this.getRailwayList().get(i);
            }

            i++;
        }

        return currentRailway;
    }

    public Railway findOppositeRailway(Railway r) {

        if(r != null) {
            return this.findRailway(r.getDestination().getName(), r.getOrigin().getName());
        }

        return null;
    }

    public void showComponent() {

        ArrayList<ArrayList<String>> components = new ArrayList<>();
        ArrayList<String> stationList = new ArrayList<>();

        CustomCollection queue = new CustomQueue();

        for(Map.Entry<String, ArrayList<Station>> entry : this.stationList.entrySet()) {
            stationList.add(entry.getKey());
        }

        while(stationList.size() > 0) {
            ArrayList<String> tmpStationsName = searchAllNeighbours(this.getStationList().get(stationList.get(0)));

            components.add(tmpStationsName);
            stationList.removeAll(tmpStationsName);
        }


        int maxComponentSize = 0;
        ArrayList<String> greaterComponent = null;

        for(ArrayList<String> component : components) {
            if(component.size() > maxComponentSize) {
                maxComponentSize = component.size();
                greaterComponent = component;
            }
        }

        System.out.println("\nNumber of component : " + components.size());
        System.out.println("Max Component size : " + maxComponentSize);

        System.out.println("One of the greater component contents : " + "\n");
        for(String s : greaterComponent) {
            System.out.println(s);
        }

        System.out.println("\nEnd of Component\n\n");
    }


    public void findCommunities() {

        ShortestPath algo = this.isWeighted() ? new DjikstraSP() : new BFSShortestPath();

        PriorityQueue<Railway> pq = new PriorityQueue<>();
        Railway opposite;

        this.showComponent();

        if(this.getRailwayList().size() == 0) {
            System.out.println("All communities found");
            return;
        }


        for (Map.Entry<String, ArrayList<Station>> entry : this.getStationList().entrySet()) {
            algo.shortestPath(this, entry.getValue().get(0).getName());

        }

        pq.addAll(this.getRailwayList());

        Railway currentRailway, firstRaylway = pq.poll();

        while(!pq.isEmpty() && (currentRailway = pq.peek()).getBetweenness() == firstRaylway.getBetweenness()) {

            opposite = this.findOppositeRailway(currentRailway);
            if(opposite != null) {
                opposite.getOrigin().getRailwayList().remove(opposite);
                this.getRailwayList().remove(opposite);
            }

            currentRailway.getOrigin().getRailwayList().remove(currentRailway);
            this.getRailwayList().remove(currentRailway);
            pq.poll();
        }

        opposite = this.findOppositeRailway(firstRaylway);
        if(opposite != null) {
            opposite.getOrigin().getRailwayList().remove(opposite);
            this.getRailwayList().remove(opposite);
        }

        firstRaylway.getOrigin().getRailwayList().remove(firstRaylway);
        this.getRailwayList().remove(firstRaylway);

        this.resetBetweenness();
        this.findCommunities();

    }

    private ArrayList<String> searchAllNeighbours(ArrayList<Station> startStations) {

        ArrayList<String> visited = new ArrayList<>();
        ArrayList<Station> currentStationList;

        CustomCollection customCollection = new CustomQueue();

        customCollection.add(startStations);

        while(!customCollection.isEmpty()) {

            currentStationList = customCollection.get();

            for(Station s : currentStationList) {

                ArrayList<Railway> neighbors = s.getRailwayList();

                if(!visited.contains(s.getName())) {
                    visited.add(s.getName());
                }

                ArrayList<Station> tmpStationList = new ArrayList<>();
                for(Railway r : neighbors) {

                    if(!visited.contains(r.getDestination().getName())) {
                        tmpStationList.addAll(this.getStationList().get(r.getDestination().getName()));
                    }


                }
                if(tmpStationList.size() > 0 ) {
                    customCollection.add(tmpStationList);
                }
            }
        }

        return visited;
    }
}