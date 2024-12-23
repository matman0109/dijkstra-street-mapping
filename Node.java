/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

public class Node implements Comparable<Node> {

    private String id;
    private double latitude;
    private double longitude;
    private double startVDist; //distance to starting vertex
    private Node prev;

    //Constructors
    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startVDist = Double.POSITIVE_INFINITY;
        this.prev = null;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.startVDist, other.startVDist);
    }

    public double getDist() {
        return startVDist;
    }

    public void setDist(double startDist) {
        this.startVDist = startDist;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
