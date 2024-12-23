/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    public URHashTable<Edge, List<Node>> adjList; // Normal adjacency list: Edge -> Nodes
    public URHashTable<Set<Node>, Edge> revAdjList; // Reverse adjacency list: Set<Node> -> Edge
    public URHashTable<String, Node> vertices; // Vertex name to Node
    public URHashTable<Node, List<Node>> adjacencyMap; // New adjacency map: Node -> Adjacent Nodes
    public ArrayList<Double> maxCoords;
    public int numVert;
    public int numEdge;

    public Graph() {
        this.adjList = new URHashTable<>();
        this.revAdjList = new URHashTable<>();
        this.vertices = new URHashTable<>();
        this.adjacencyMap = new URHashTable<>();
        this.maxCoords = new ArrayList<>();
        this.numVert = 0;
        this.numEdge = 0;

        readFile("/Users/matman0109/IdeaProjects/CSC 173 Project 3_2/src/monroe.txt");
    }

    //methods to find the best graph size
    public double getMaxLatitude() {
        return maxCoords.get(0);
    }

    public double getMinLatitude() {
        return maxCoords.get(1);
    }

    public double getMaxLongitude() {
        return maxCoords.get(2);
    }

    public double getMinLongitude() {
        return maxCoords.get(3);
    }

    //reads file, creating Nodes and Edges and putting them in the appropriate HashTables and Lists
    public void readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            double maxLatitude = Double.MIN_VALUE;
            double minLatitude = Double.MAX_VALUE;
            double maxLongitude = Double.NEGATIVE_INFINITY;
            double minLongitude = Double.MAX_VALUE;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");

                String type = parts[0];
                String name = parts[1];
                String arg1 = parts[2];
                String arg2 = parts[3];

                if (type.equals("i")) {
                    double latitude = Double.parseDouble(arg1);
                    double longitude = Double.parseDouble(arg2);
                    Node node = new Node(name, latitude, longitude);
                    vertices.put(name, node);
                    adjacencyMap.put(node, new ArrayList<>());
                    numVert++;

                    if (latitude > maxLatitude) {
                        maxLatitude = latitude;
                    }
                    if (latitude < minLatitude) {
                        minLatitude = latitude;
                    }
                    if (longitude > maxLongitude) {
                        maxLongitude = longitude;
                    }
                    if (longitude < minLongitude) {
                        minLongitude = longitude;
                    }
                }
                else if (type.equals("r")) {
                    Node vertex1 = findVertex(arg1);
                    Node vertex2 = findVertex(arg2);
                    Edge edge = new Edge(name, vertex1, vertex2);

                    // Update adjList and revAdjList
                    ArrayList<Node> nodes = new ArrayList<>();
                    nodes.add(vertex1);
                    nodes.add(vertex2);
                    adjList.put(edge, nodes);

                    Set<Node> nodesForRevAdjList = new HashSet<>();
                    nodesForRevAdjList.add(vertex1);
                    nodesForRevAdjList.add(vertex2);
                    revAdjList.put(nodesForRevAdjList, edge);

                    // Update adjacencyMap for both vertices
                    adjacencyMap.get(vertex1).add(vertex2);
                    adjacencyMap.get(vertex2).add(vertex1);

                    numEdge++;
                }
            }
            maxCoords.add(maxLatitude);
            maxCoords.add(minLatitude);
            maxCoords.add(maxLongitude);
            maxCoords.add(minLongitude);

        } catch (IOException e) {
        }
    }

    public Node findVertex(String vertName) {
        return vertices.get(vertName);
    }

    // Method for debugging
    public void printAdjList() {
        // Iterate through each Edge key in the adjList
        for (Edge edge : adjList) {
            List<Node> connectedNodes = adjList.get(edge);

            // Print the Edge ID
            System.out.print("Edge " + edge.getId() + " connects: ");

            // Print the IDs of the connected Nodes
            for (Node node : connectedNodes) {
                System.out.print(node.getId() + " ");
            }
            System.out.println();
        }
    }

    // Finds distance between currentV and adjV
    public double findEdgeDist(Node currentV, Node adjV) {

        Set<Node> nodes = new HashSet<>();
        nodes.add(currentV);
        nodes.add(adjV);
        //uses .get() method providing O(1) retrieval
        Edge edge = revAdjList.get(nodes);
        if (edge != null && (
                (edge.getVertex1() == currentV && edge.getVertex2() == adjV) ||
                        (edge.getVertex1() == adjV && edge.getVertex2() == currentV))) {
            return edge.getEdgeDist();
        }
        else if (currentV == adjV) {
            return 0;
        }
        return Double.POSITIVE_INFINITY; // Return a large value if no edge exists
    }

    // Optimized method to find adjacent nodes
    public ArrayList<Node> findAdjNodes(Node currentV) {
        //uses .get() method providing O(1) retrieval
        List<Node> adjNodes = adjacencyMap.get(currentV);
        if (adjNodes != null) {
            return new ArrayList<>(adjNodes);
        }
        return new ArrayList<>(); // Return an empty list if no adjacent nodes are found
    }

    public Node findNodeById(String id) {
        return vertices.get(id);
    }

    //finds shortest path using Dijkstra's algorithm
    public List<Node> findShortestPath(String startId, String finishId) {
        Node startV = findNodeById(startId);
        PriorityQueue<Node> unvisited = new PriorityQueue<>();

        startV.setDist(0);
        unvisited.enqueue(startV);

        while (!unvisited.isEmpty()) {
            Node currentV = unvisited.dequeue();

            for (Node adjV : findAdjNodes(currentV)) {
                double edgeWeight = findEdgeDist(currentV, adjV);
                double altPathDistance = currentV.getDist() + edgeWeight;

                if (altPathDistance < adjV.getDist()) {
                    adjV.setDist(altPathDistance);
                    adjV.setPrev(currentV);

                    unvisited.enqueue(adjV); // Re-adding the vertex with the updated distance
                }
            }
        }

        Node finishNode = vertices.get(finishId);
        Node currentNode = finishNode;
        Stack<Node> nodeStack = new Stack<>();

        while (currentNode != null) {
            nodeStack.push(currentNode);
            currentNode = currentNode.getPrev();
        }

        List<Node> returnList = new ArrayList<>();
        System.out.print("Path: ");
        while (!nodeStack.isEmpty()) {
            Node node = nodeStack.pop();
            System.out.print(node.getId());
            returnList.add(node);
            if (!nodeStack.isEmpty()) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("Total travelled: " + String.format("%.2f", finishNode.getDist()) + " miles.");
        return returnList;
    }
}
