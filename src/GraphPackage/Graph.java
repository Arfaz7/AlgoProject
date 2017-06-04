package GraphPackage;

import HelperPackage.FileReaderHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arfaz on 15/05/2017.
 */
public class Graph extends AbstractGraph{

    private final String SUBWAY_FOLDER = "Documents/SUBWAY_LINES/";
    private final String STATIONS_FILENAME = "/stops.txt";
    private final String RAILWAYS_FILENAME = "/stop_times.txt";

    private HashMap<String, Subway> subwayList;


    public Graph(boolean weighted) {
        super(weighted);

        this.subwayList = new HashMap<>();

        this.generateGraph();
    }

    public HashMap<String, Subway> getSubwayList() {
        return subwayList;
    }

    public void setSubwayList(HashMap<String, Subway> subwayList) {
        this.subwayList = subwayList;
    }


    private void generateGraph() {

        // Create stations

        File folder = new File(SUBWAY_FOLDER);
        File[] folderList = folder.listFiles();


        for (int i = 0; i < folderList.length; i++) {
            if (folderList[i].isDirectory()) {
                String fileName = folderList[i].getName();
                int fileNameLength = fileName.split("_").length - 2;

                if (fileNameLength >= 0 && fileName.split("_")[fileNameLength].equals("METRO")) {

                    System.out.print("Import stations from : " + fileName.split("_")[fileNameLength]
                                                            + " " + fileName.split("_")[fileNameLength + 1]);

                    // Create Subway and its stations
                    Subway sub = new Subway(fileName, this.isWeighted());

                    List<String[]> stationLines = FileReaderHelper.importStationsFromFile(SUBWAY_FOLDER
                                                    + folderList[i].getName() + STATIONS_FILENAME);
                    ArrayList<Station> stations = this.createStation(stationLines);
                    sub.generateStationList(stations);

                    this.subwayList.put(fileName, sub);

                    System.out.println(" Done");


                    System.out.print("Import railways from : " + fileName.split("_")[fileNameLength] + " "
                                        + fileName.split("_")[fileNameLength + 1]);

                    // Create Railways
                    ArrayList<ArrayList<String>> railwayLines = FileReaderHelper.importRailwayFromFile(SUBWAY_FOLDER
                                                                + folderList[i].getName() + RAILWAYS_FILENAME);

                    this.createRailway(sub, railwayLines);
                    System.out.println(" Done");
                }
            }
        }

        System.out.println("\n");
    }

    private ArrayList<Station> createStation(List<String[]> lines) {

        ArrayList<Station> currentStationList = new ArrayList<>();
        Station currentStation = null;

        try {

            for (String[] currentLine : lines) {

                if ((currentStation = this.stationExists(currentStationList, currentLine[2])) == null) {

                    currentStation = new Station(Integer.parseInt(currentLine[0]), currentLine[2], currentLine[3],
                                            Double.parseDouble(currentLine[4]), Double.parseDouble(currentLine[5]));

                    currentStationList.add(currentStation);

                    if(this.getStationList().get(currentLine[2]) == null) {
                        this.getStationList().put(currentLine[2], new ArrayList<>());
                    }

                    this.getStationList().get(currentLine[2]).add(currentStation);

                } else {
                    currentStation.getIdList().add(Integer.parseInt(currentLine[0]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return currentStationList;
        }
    }

    private Station stationExists(ArrayList<Station> stationList , String stationName) {
        Station station = null;
        int i = 0;

        while (station == null && i < stationList.size()) {

            if (stationName.equals(stationList.get(i).getName())) {
                station = stationList.get(i);
            }

            i++;
        }

        return station;
    }

    private void createRailway(Subway currentSubway, ArrayList<ArrayList<String>> lines) {

        ArrayList<Railway> currentRailwayList = new ArrayList<>();

        for (ArrayList<String> line : lines) {

            int i = 1;

            while(i < line.size()) {
                Integer fromId = Integer.parseInt(line.get(i-1));
                Integer toId = Integer.parseInt(line.get(i));

                if(!this.railwayExists(currentRailwayList, fromId, toId)) {

                    Railway tmpRailway = new Railway(currentSubway.getStationById(fromId), currentSubway.getStationById(toId), this.isWeighted());

                    currentRailwayList.add(tmpRailway);
                    this.getRailwayList().add(tmpRailway);
                }

                i++;
            }
        }

        currentSubway.getRailwayList().addAll(currentRailwayList);
    }

    private boolean railwayExists(ArrayList<Railway> railwayList, Integer fromId, Integer toId) {

        boolean exists = false;

        for(Railway r : railwayList) {

            if(r.getOrigin().getIdList().contains(fromId) && r.getDestination().getIdList().contains(toId)) {
                exists = true;
            }
        }

        return exists;
    }
}