package GraphPackage;

import org.omg.CORBA.MARSHAL;

/**
 * Created by arfaz on 15/05/2017.
 */
public class Railway implements Comparable {

    private final int EARTH_RADIUS = 6371;
    private Station origin;
    private Station destination;
    private double distance;
    private int betweenness;

    public Railway(Station origin, Station destination, boolean weighted) {
        this.origin = origin;
        this.destination = destination;
        this.betweenness = 0;

        this.origin.getRailwayList().add(this);

        this.distance = weighted ? this.calculateDistance() : 1.0;
    }

    public Station getOrigin() {
        return origin;
    }

    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getBetweenness() {
        return betweenness;
    }

    public void setBetweenness(int betweenness) {
        this.betweenness = betweenness;
    }

    private double calculateDistance() {

        double dLat = this.convertDegreeToRadian(this.destination.getLatitude() - this.origin.getLatitude());
        double dLong = this.convertDegreeToRadian(this.destination.getLongitude() - this.origin.getLongitude());

        double tmpCalcul = (Math.sin(dLat/2) * Math.sin(dLat/2))
                                + (Math.cos(convertDegreeToRadian(this.origin.getLatitude()))
                                * Math.cos(convertDegreeToRadian(this.destination.getLatitude()))
                                * Math.sin(dLong/2) * Math.sin(dLong/2)
                            );
        double angle = 2 * Math.atan2(Math.sqrt(tmpCalcul), Math.sqrt(1-tmpCalcul));

        // Distance in meter
        double distance = EARTH_RADIUS * angle * 1000;

        return distance;
    }

    private double convertDegreeToRadian(double degree) {

        return degree * (Math.PI / 180);
    }

    @Override
    public int compareTo(Object o) {

        if(!(o instanceof Railway)) {
            return 1;
        }

        return this.getBetweenness() < ((Railway) o).getBetweenness() ? 1 : -1;

    }
}
