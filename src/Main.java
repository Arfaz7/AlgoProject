import GraphPackage.Graph;
import HelperPackage.UnzipHelper;
import ShortestPathPackage.BFSShortestPath;
import ShortestPathPackage.DjikstraSP;
import ShortestPathPackage.ShortestPath;

/**
 * Created by arfaz on 15/05/2017.
 */
public class Main {

    public static void main(String[] args) {

        UnzipHelper.unzip();

        Graph g = new Graph(false);

        // Test BFS and Djikstra Algorithms

        ShortestPath sp;

        if(!g.isWeighted()) {
            sp = new BFSShortestPath();
        }

        else {
            sp = new DjikstraSP();
        }

        sp.shortestPath(g, "Bastille");

        System.out.println(sp.showMarked());
        System.out.println(sp.showDistance());
        System.out.println(sp.showPrevious());

        sp.longestPath(g.getSubwayList().get("METRO_1"));


        // Test Communities detection and component identification
        g.resetBetweenness();
        g.showComponent();
        g.findCommunities();
    }

}
