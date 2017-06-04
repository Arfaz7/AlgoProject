package HelperPackage;

import com.opencsv.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by arfaz on 15/05/2017.
 */
public class FileReaderHelper {

    public static List<String[]> importStationsFromFile(String filePath) {

        List<String[]> stationList = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(filePath), ',', '\"', 1);
            stationList = reader.readAll();
            reader.close();

        } catch (Exception e) {

            System.out.println("Import station : An error occured");
            e.printStackTrace();
        } finally {

            return stationList;
        }
    }

    public static ArrayList<ArrayList<String>> importRailwayFromFile(String filePath) {

        ArrayList<ArrayList<String>> railwayPaths = new ArrayList<>();
        ArrayList<String> tmpRailwayList = new ArrayList<>();

        try {

            BufferedReader buffer = new BufferedReader(new FileReader(filePath));
            int currentSequence = 0;
            String line;

            buffer.readLine();
            while((line =  buffer.readLine()) != null) {

                String[] splittedLine = line.split(",");

                if(Integer.parseInt(splittedLine[splittedLine.length - 1]) < currentSequence) {
                    if(!isExistingPath(railwayPaths, tmpRailwayList)) {
                        railwayPaths.add(((ArrayList<String>)tmpRailwayList.clone()));

                        Collections.sort(railwayPaths, new Comparator<ArrayList>() {
                            public int compare(ArrayList a1, ArrayList a2) {
                                return a2.size() - a1.size(); // tri la list de list par ordre decroissant de leur taille
                            }
                        });
                    }

                    tmpRailwayList.clear();
                }

                currentSequence = Integer.parseInt(splittedLine[splittedLine.length - 1]);
                tmpRailwayList.add(splittedLine[splittedLine.length - 2]);
            }

            removeExistingSubPath(railwayPaths);

            buffer.close();

        } catch (Exception e) {

            System.out.println("Import railways : An error occured");
            e.printStackTrace();
        } finally {

            return railwayPaths;
        }
    }

    private static boolean isExistingPath(ArrayList<ArrayList<String>> allPathList, ArrayList<String> path) {
        boolean exists = false;

        for(ArrayList<String> currentPath : allPathList) {
            if(Arrays.equals(currentPath.toArray(), path.toArray()) || Arrays.asList(currentPath.toArray()).containsAll(Arrays.asList(path.toArray()))) {
                exists = true;
            }
        }

        return exists;
    }

    private static void removeExistingSubPath(ArrayList<ArrayList<String>> allPaths) {

        ArrayList<String> currentList, nextList, tmpList, tmpListIntersect, reversedList;

        int i =1;

        while(i < allPaths.size()) {
            int j = 0;
            boolean cleaned = false;

            nextList = allPaths.get(i);
            tmpList = (ArrayList<String>)nextList.clone();
            tmpListIntersect = (ArrayList<String>)nextList.clone();

            while(j < i && !cleaned) {
                boolean reversed = false;

                currentList = allPaths.get(j);

                tmpListIntersect.retainAll(currentList);
                tmpList.removeAll(tmpListIntersect);

                if(tmpList.size() == nextList.size()) {
                    reversed = true;

                    tmpListIntersect = (ArrayList<String>)nextList.clone();
                    Collections.reverse(tmpListIntersect);
                    tmpListIntersect.retainAll(currentList);
                    tmpList.removeAll(tmpListIntersect);
                }

                if(tmpList.size() > 0 && tmpList.size() < nextList.size()) {

                    int minIndex = nextList.indexOf(tmpList.get(0)), maxIndex = nextList.indexOf(tmpList.get(0));

                    if(nextList.indexOf(tmpList.get(0)) > 0) {
                        minIndex = nextList.indexOf(tmpList.get(0)) -1;
                    }

                    if(nextList.indexOf(tmpList.get(tmpList.size()-1)) < nextList.size()) {
                        maxIndex = nextList.indexOf(tmpList.get(tmpList.size() - 1)) + 1;

                        if (nextList.indexOf(tmpList.get(tmpList.size() - 1)) < nextList.size() - 1) {
                            maxIndex++;
                        }

                    }

                    allPaths.add(allPaths.indexOf(nextList), new ArrayList<String>(nextList.subList(minIndex, maxIndex)));

                    cleaned = true;
                    allPaths.remove(nextList);
                }

                j++;
            }
            i++;
        }
    }
}
