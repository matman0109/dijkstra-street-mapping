/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StreetMap {

    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }

        String mapFile = args[0];
        boolean showMap = false;
        boolean findDirections = false;
        String startIntersection = null;
        String endIntersection = null;

        // Parse command-line arguments
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--show":
                    showMap = true;
                    break;
                case "--directions":
                    findDirections = true;
                    if (i + 2 < args.length) {
                        startIntersection = args[++i];
                        endIntersection = args[++i];
                    } else {
                        System.out.println("Error: Missing start or end intersection for directions.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Unknown argument: " + args[i]);
                    return;
            }
        }

        // Load the graph
        Graph graph = new Graph();
        graph.readFile(mapFile);

        // Handle --directions
        List<Node> shortestPath = null;
        if (findDirections) {
            shortestPath = graph.findShortestPath(startIntersection, endIntersection);
            if (shortestPath == null) {
                System.out.println("No path found between " + startIntersection + " and " + endIntersection);
                return;
            }
        }

        // Handle --show
        if (showMap) {
            double maxLat = graph.getMaxLatitude();
            double minLat = graph.getMinLatitude();
            double maxLon = graph.getMaxLongitude();
            double minLon = graph.getMinLongitude();

            JFrame frame = new JFrame("Street Map");
            GraphVisualizer visualizer = new GraphVisualizer(graph, maxLat, minLat, maxLon, minLon);

            visualizer.enableMapDrawing();
            if (findDirections && shortestPath != null) {
                visualizer.enableShortestPathDrawing(shortestPath);
            }

            frame.add(visualizer);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // sets the initial window size and make it resizable
            frame.setSize(800, 600); // Initial size
            double aspectRatio = 16.0 / 10.0; // Aspect ratio of the window

            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension size = frame.getSize();
                    int newWidth = size.width;
                    int newHeight = (int) (newWidth / aspectRatio);

                    // Adjusts height if needed
                    if (newHeight > size.height) {
                        newHeight = size.height;
                        newWidth = (int) (newHeight * aspectRatio);
                    }

                    frame.setSize(newWidth, newHeight);
                }
            });

            frame.setVisible(true);
        }
    }
}
