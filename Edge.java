/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

//code for calculateDistance() is from here: https://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates

public class Edge implements Comparable<Edge> {
    private String id;
    private Node vertex1;
    private Node vertex2;
    private double dist;

    public Edge(String id, Node vertex1, Node vertex2) {
        this.id = id;
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.dist = calculateDistance();
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.dist, other.dist);
    }

    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    //Uses Haversine formula to calculate distnaces between coordinates
    private double calculateDistance() {
        double lat1 = vertex1.getLatitude();
        double lon1 = vertex1.getLongitude();
        double lat2 = vertex2.getLatitude();
        double lon2 = vertex2.getLongitude();

        final double earthRadiusKm = 6371.0;
        final double kmToMilesConversionFactor = 0.621371;

        double dLat = degreesToRadians(lat2 - lat1);
        double dLon = degreesToRadians(lon2 - lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceInKm = earthRadiusKm * c;
        return distanceInKm * kmToMilesConversionFactor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getVertex1() {
        return vertex1;
    }

    public void setVertex1(Node vertex1) {
        this.vertex1 = vertex1;
    }

    public Node getVertex2() {
        return vertex2;
    }

    public void setVertex2(Node vertex2) {
        this.vertex2 = vertex2;
    }

    public double getEdgeDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }
}
