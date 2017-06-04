package ShortestPathPackage;

import CustomCollectionPackage.CustomCollection;
import CustomCollectionPackage.CustomQueue;
import GraphPackage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by arfaz on 28/05/2017.
 */
public class DjikstraSP extends ShortestPath {


    public void shortestPath(AbstractGraph g, String s) {
        this.marked = new HashMap<>();
        this.previous = new HashMap<>();
        this.distance = new HashMap<>();

        this.source = s;

        ArrayList<Station> currentStationList, tmpStationList;

        CustomCollection customCollection;

        try {
            customCollection = new CustomQueue();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(( tmpStationList = g.getStationList().get(this.source)) != null && this.verifyNonNegative(g)) {
            customCollection.add(tmpStationList);
            this.previous.put(s, null);
            this.distance.put(s, .0);
        }

        else {
            System.out.println("Cette Station n'existe pas.");
            return;
        }

        while(!customCollection.isEmpty()) {
            currentStationList = customCollection.get();
            this.marked.put(currentStationList.get(0).getName() ,true);

            ArrayList<Railway> neighbors = new ArrayList<>();

            for(Station tmpStation : currentStationList) {
                neighbors.addAll(tmpStation.getRailwayList());
            }

            for(Railway r : neighbors) {

                if(this.distance.get(r.getDestination().getName()) == null || this.distance.get(r.getDestination().getName()) >
                        this.distance.get(currentStationList.get(0).getName())+r.getDistance()) {
                    customCollection.add(g.getStationList().get(r.getDestination().getName()));
                    this.distance.put(r.getDestination().getName(), this.distance.get(currentStationList.get(0).getName()) + r.getDistance());
                    this.previous.put(r.getDestination().getName(), currentStationList.get(0).getName());
                }
            }
        }

        this.initiateBetweenness(g);
    }


    public void longestPath(AbstractGraph g) {

        double maxPathLength = 0.;
        String origin = "", destination = "";
        HashMap<String, String> previousMap = new HashMap<>();


        for (Map.Entry<String, ArrayList<Station>> entry : g.getStationList().entrySet()) {

            this.shortestPath(g, entry.getValue().get(0).getName());

            for (Map.Entry<String, Double> dist : this.distance.entrySet()) {
                if (dist.getValue() > maxPathLength) {
                    origin = entry.getValue().get(0).getName();
                    maxPathLength = dist.getValue();
                    destination = dist.getKey();
                    previousMap = this.getPrevious();
                }
            }
        }


        String longestPath = "Longest path : " + destination;
        String currentStationName = destination;

        ArrayList<String> subPathList = new ArrayList<>();

        while (!currentStationName.equals(origin)) {
            String originStation = currentStationName;

            currentStationName = previousMap.get(currentStationName);
            subPathList.add(originStation + " - " + currentStationName + " : "
                    + g.getRailwayDistance(originStation, currentStationName) + "m");

            longestPath += " - " + currentStationName;

        }

        System.out.println(longestPath + "\n");

        System.out.println("Total length of the path : " + maxPathLength + "m" + "\n");

        System.out.println("Lengths of sub-paths : " + "\n");

        for (String s : subPathList) {
            System.out.println(s);
        }

    }

    public boolean verifyNonNegative(AbstractGraph g) {

        boolean notNegative = true;
        int i = 0;

        while (i < g.getRailwayList().size() && notNegative) {

            if(g.getRailwayList().get(i).getDistance() < 0) {
                notNegative = false;
            }

            i++;
        }

        return notNegative;
    }
}