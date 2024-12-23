https://github.com/matman0109
credits for one of the methods in Edge.java to https://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates

This program takes in a tab-delimited txt file containing intersections with real world coordinates and roads connecting those intersections. User passes names of two intersections via command line terminal and if a path between the two intersections exists, the program uses Dijkstra's algorithm to find the shortest path between them.

Sample intersection:
i	intersection_name	43.128934	-77.630132

Sample road:
r	road_name	intersection1_name	intersection2_name

Sample command-line input:
java StreetMap map_name.txt --show --directions startIntersection endIntersection

In the input above:
--show - displays a map and path(optional)
--directions - builds a path between two intersections
So input might look like this:
java StreetMap ur.txt --show --directions HOYT MOREY //Showing both map
and the directions
java StreetMap ur.txt --show //Just showing the map
java StreetMap ur.txt --directions HOYT MOREY //Showing the map is
optional


List of files:
1) monroe.txt - file containing intersections and roads in Monroe county of New York State
2) ur.txt - file containing intersections and roads of University of Rochester River campus
3) Edge.java - custom edge implementation for my Graph data structure
4) Node.java - custom node implementation for my Graph data structure
5) Graph.java - custom implementation of Graph data structure (based on adjacency list)
6) PriorityQueue.java - custom priority queue implementaiton
7) URHashTable.java - custom implementation of hash table 
8) UR_HashTable.java - abstract class for hash table that was provided as part of the assignment to make this project
9) GraphVisualizer.java - contains most of the logic for drawing the map via Java Swing and AWT
10) StreetMap.java - contains main method that ensures proper window management and command line input processing
11) README.txt


Runtime:
-plotting the map - O(E)
-finding the shortest path between two intersections - O(VlogV)


Synopsis:
Before you ask - the program uses custom-built Edge, Node, and Graph classes. On top of that, I've used a custom HashMap(URHashTable) and PriorityQueue(same name) implementations that I created for previous labs.

Graph class is used for all the path building logic. First, readFile() goes through the txt file provided and depending on whether the line provided is info about vertex or edge, does the following:
Vertex - creates new vertices using name, latitude and longitude provided and stores those vertices(nodes) in a HashTable.
Edge - creates a new edge that stores the name and two pointers to the vertices it connects.

Next up, the main method calls the findShortestPath() method of the graph to find the shortest path from one point to another. The method follows the standard procedure of using Dijkstra's greedy algorithm as described by ZyBooks > CSC 172 > 10.9.1. A few comments need to be made on the methods used here:
findAdjNodes - takes the current vertex at that point in iteration and uses adjList containing edges and their pointers to vertices in order to locate the right edge based on the vertex combination. This way it's able to locate vertices that are directly adjacent to the current vertex.
findEdgeDist - takes current vertex and its adjacent vertex and finds the appropriate edge based on their combination. It returns the distance value for that edge.

Continuing along the findShortestPath() method, we follow the prev pointers of each node (vertex) now correctly set up to provide the shortest path from point A to point B. The path is in reverse order and to put it in the right order i've used a stack.

Finally, the path is printed out and total miles travelled rounded to two decimal figures.


Visualization:
The GraphVisualizer class is a custom Swing JPanel designed to visually represent the graph. It allows me to draw the graph's edges, nodes, and the shortest path between two points via a red line. This class is tightly integrated with the Graph class and Node and Edge classes. It calculates latitude and longitude bounds to scale and fit the graph within the visualization panel and maintains consistent aspect ratios for accurate geographical representation. The StreetMap class basically makes it all work as intended.

Difficulties i ran into:
One of the hardest problems for me was finding the adjacent nodes, which is why i implemented findAdjNodes() method in the graph class that would go on and compare the pairs of nodes withing Edges until the right pair was found. I had the same issue with findEdgeDist() method that based on the pair of nodes would find me the right edge and return it's distance value. Those methods were really inefficient, resulting in the nested loops being created that tanked my performance. To solve this, with the help of lab TAs I made a copy of AdjList that contains all node-edge pairs, but reversed it so the keys would switch places with values. This allowed me to utilize my URHashList's get() method that has O(1) time complexity allowing me to go from snail to lightning speed for larger datasets. More or less the same strategy was used for findAdjNodes().

Another issue I ran into was the map being too zoomed in for larger data sets. That's why now to get an idea of how big the graph is gonna be I implemented helper methods to find the largest and smallest latitudes and longitudes.