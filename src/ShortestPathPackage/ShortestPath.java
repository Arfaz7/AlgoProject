package ShortestPathPackage;

import GraphPackage.AbstractGraph;
import GraphPackage.Graph;
import GraphPackage.Railway;
import GraphPackage.Station;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arfaz on 28/05/2017.
 */
public abstract class ShortestPath {
    protected HashMap<String, Boolean> marked;
    protected HashMap<String, String> previous;
    protected HashMap<String, Double> distance;
    protected String source;


    public HashMap<String, Boolean> getMarked() {
        return marked;
    }

    public void setMarked(HashMap<String, Boolean> marked) {
        this.marked = marked;
    }

    public HashMap<String, String> getPrevious() {
        return previous;
    }

    public void setPrevious(HashMap<String, String> previous) {
        this.previous = previous;
    }

    public HashMap<String, Double> getDistance() {
        return distance;
    }

    public void setDistance(HashMap<String, Double> distance) {
        this.distance = distance;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public abstract void shortestPath(AbstractGraph g, String stationStart);

    public abstract void longestPath(AbstractGraph g);

    public String showMarked() {
        String s = "";

        for(Map.Entry<String, Boolean> entry : this.marked.entrySet()) {
            s += entry.getKey() + " | " + entry.getValue() + "\n";
        }

        return s;
    }

    public String showPrevious() {
        String s = "";

        for(Map.Entry<String, String> entry : this.previous.entrySet()) {
            s += entry.getKey() + " | " + entry.getValue() + "\n";
        }

        return s;
    }

    public String showDistance() {
        String s = "";

        for(Map.Entry<String, Double> entry : this.distance.entrySet()) {
            s += entry.getKey() + " | " + entry.getValue() + "\n";
        }

        return s;
    }

    public boolean hasPathTo(String destination) {

        boolean b = false;
        String previous = this.previous.get(destination);

        while(previous != null  && !b) {

            if(previous.equals(this.source)) {
                b=true;
            }

            previous = this.previous.get(previous);
        }

        return b;
    }

    public Double distTo(String destination) {

        return this.distance.get(destination);
    }

    public void printSP(String dest) {
        String s = "";
        ArrayList<String> nodeList = new ArrayList<>();
        String current = dest;
        nodeList.add(dest);

        if(this.hasPathTo(dest)) {
            while(!current.equals(this.source)) {
                current = this.previous.get(current);
                nodeList.add(current);
            }
        }

        if(nodeList.size() > 1) {
            for (int i=nodeList.size()-1; i>=0; i--) {
                s += nodeList.get(i) + " ";
            }
        }

        else {
            s = "No path from " + this.source + " to " + dest;
        }
        System.out.println(s);
    }

    protected void initiateBetweenness(AbstractGraph g) {
        for (Map.Entry<String, String> entry : this.previous.entrySet()) {

            Railway currentRailway = null;

            if(entry.getValue() != null) {
                currentRailway = g.findRailway(entry.getValue(), entry.getKey());
                currentRailway.setBetweenness(currentRailway.getBetweenness() +1);
            }

            currentRailway = g.findOppositeRailway(currentRailway);

            if(currentRailway != null) {
                currentRailway.setBetweenness(currentRailway.getBetweenness()+1);
            }

        }
    }
}
