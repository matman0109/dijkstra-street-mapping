/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GraphVisualizer extends JPanel {

    private Graph graph;
    private List<Edge> edges;
    private List<Node> shortestPath;

    private boolean drawMap;
    private boolean drawShortestPath;

    // Instance variables for coordinate bounds
    private double maxLat;
    private double minLat;
    private double maxLon;
    private double minLon;

    public GraphVisualizer(Graph graph, double maxLat, double minLat, double maxLon, double minLon) {
        this.graph = graph;
        this.edges = new ArrayList<>(graph.adjList.size());
        this.shortestPath = new ArrayList<>();
        this.drawMap = false;
        this.drawShortestPath = false;

        // Populate the edges list from the graph's adjList
        for (Edge edge : graph.adjList) {
            edges.add(edge);
        }

        // Initialize coordinate bounds
        this.maxLat = maxLat;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.minLon = minLon;

        // Maintains aspect ratio
        setPreferredSize(new Dimension(800, 600));
    }

    public void enableMapDrawing() {
        this.drawMap = true;
        repaint();
    }


    public void enableShortestPathDrawing(List<Node> path) {
        this.shortestPath = path;
        this.drawShortestPath = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //draws the map
        if (drawMap) {
            g2.setColor(Color.BLACK);
            for (Edge edge : edges) {
                Node v1 = edge.getVertex1();
                Node v2 = edge.getVertex2();
                Point p1 = convertToPixel(v1);
                Point p2 = convertToPixel(v2);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        //draws the red line showing shortest path
        if (drawShortestPath) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                Node v1 = shortestPath.get(i);
                Node v2 = shortestPath.get(i + 1);
                Point p1 = convertToPixel(v1);
                Point p2 = convertToPixel(v2);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    //converts coords to pixels
    private Point convertToPixel(Node node) {
        double latitude = node.getLatitude();
        double longitude = node.getLongitude();

        // Normalize lat/long to fit panel dimensions
        int x = (int) ((longitude - minLon) / (maxLon - minLon) * getWidth());
        int y = (int) ((maxLat - latitude) / (maxLat - minLat) * getHeight());

        return new Point(x, y);
    }
}
