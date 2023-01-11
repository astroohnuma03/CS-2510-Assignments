import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//interface representing searching
interface ICollection<T> {

  // checks if this collection is empty
  boolean isEmpty();

  // adds the given item to this collection
  void add(T item);

  // removes the first item from this collection
  T remove();
}

//abstract class representing searching
abstract class ACollection<T> implements ICollection<T> {
  Deque<T> contents;

  ACollection() {
    this.contents = new ArrayDeque<T>();
  }

  // returns true if the contents deque is empty
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // removes and returns the first element in the contents deque
  public abstract T remove();

  // adds the given item to the front of the contents deque
  public void add(T item) {
    this.contents.addFirst(item);
  }
}

//represents a stack to be used in a depth-first search
class Stack<T> extends ACollection<T> {
  Stack() {
    this.contents = new ArrayDeque<T>();
  }

  // adds the given item to the front of the contents deque
  @Override
  public void add(T item) {
    this.contents.addFirst(item);
  }

  // removes the first item from the stack and returns it
  public T remove() {
    return this.contents.pop();
  }
}

//represents a queue to be used in a breadth-first search
class Queue<T> extends ACollection<T> {
  Queue() {
    this.contents = new ArrayDeque<T>();
  }

  // adds the given item to the tail of the contents deque
  @Override
  public void add(T item) {
    this.contents.addLast(item);
  }

  // removes the first item from the queue and returns it
  public T remove() {
    return this.contents.removeFirst();
  }
}

// to represent the vertexes in the graph
class Vertex {
  int x;
  int y;
  Vertex left;
  Vertex top;
  Vertex right;
  Vertex bottom;
  Color color;
  ArrayList<Edge> outEdges;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
    this.color = Color.gray;
    this.outEdges = new ArrayList<Edge>();
  }

  Vertex(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
    this.color = color;
    this.outEdges = new ArrayList<Edge>();
  }

  // draw this vertex
  WorldImage drawVertex(Color color) {
    int horizLineX = (int) (20 * Math.cos(Math.toRadians(180 - 0)));
    int horizLineY = (int) (20 * Math.sin(Math.toRadians(180 - 0)));
    int verticLineX = (int) (20 * Math.cos(Math.toRadians(180 - 90)));
    int verticLineY = (int) (20 * Math.sin(Math.toRadians(180 - 90)));
    WorldImage horizLine = new LineImage(new Posn(horizLineX, horizLineY), Color.black);
    WorldImage verticLine = new LineImage(new Posn(verticLineX, verticLineY), Color.black);
    WorldImage vertex = new RectangleImage(20, 20, OutlineMode.SOLID, color);
    if (left == null || this.findEdge(left) == null && left.findEdge(this) == null) {
      vertex = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, verticLine, 0, 0, vertex);
    }
    if (bottom == null || this.findEdge(bottom) == null && bottom.findEdge(this) == null) {
      vertex = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, horizLine, 0, 0,
          vertex);
    }
    if (right == null || this.findEdge(right) == null && right.findEdge(this) == null) {
      vertex = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, verticLine, 0, 0,
          vertex);
    }
    if (top == null || this.findEdge(top) == null && top.findEdge(this) == null) {
      vertex = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, horizLine, 0, 0, vertex);
    }
    return vertex;
  }

  // places this vertex on to the given scene
  public WorldScene place(WorldScene scene) {
    scene.placeImageXY(this.drawVertex(Color.gray), this.x, this.y);
    return scene;
  }

  // checks if this vertex and the given vertex create an edge,
  // and create that edge if so
  public Edge createEdge(Vertex other, boolean rand) {
    Edge seenEdge = this.findEdge(other);
    Edge seenOtherEdge = other.findEdge(this);
    if (right != null && right.equals(other) && seenEdge == null && seenOtherEdge == null) {
      if (rand) {
        Edge edge = new Edge(this, other);
        Edge edgeOther = new Edge(other, this);
        if (!outEdges.contains(edge)) {
          outEdges.add(edge);
        }
        if (!other.outEdges.contains(edgeOther)) {
          other.outEdges.add(edgeOther);
        }
        return edge;
      }
      else {
        Edge edge = new Edge(this, other, false);
        outEdges.add(edge);
        return edge;
      }
    }
    else if (bottom != null && bottom.equals(other) && seenEdge == null && seenOtherEdge == null) {
      if (rand) {
        Edge edge = new Edge(this, other);
        Edge edgeOther = new Edge(other, this);
        if (!outEdges.contains(edge)) {
          outEdges.add(edge);
        }
        if (!other.outEdges.contains(edgeOther)) {
          other.outEdges.add(edgeOther);
        }
        return edge;
      }
      else {
        Edge edge = new Edge(this, other, false);
        outEdges.add(edge);
        return edge;
      }
    }
    else {
      return null;
    }
  }

  // checks if an edge in the outEdges list has the given vertex
  // as its "to" field and returns it if it does, returning null otherwise
  public Edge findEdge(Vertex other) {
    Edge edge = null;
    for (Edge i : outEdges) {
      if (i.to.equals(other)) {
        edge = i;
      }
    }
    return edge;
  }

  // checks if the given vertex is in any way connected to this vertex
  // and mutates both vertices to reflect this
  public void connect(Vertex other) {
    if (this.x - 20 == other.x && this.y == other.y) {
      this.left = other;
      other.right = this;
    }
    else if (this.x == other.x && this.y - 20 == other.y) {
      this.top = other;
      other.bottom = this;
    }
    else if (this.x + 20 == other.x && this.y == other.y) {
      this.right = other;
      other.left = this;
    }
    else if (this.x == other.x && this.y + 20 == other.y) {
      this.bottom = other;
      other.top = this;
    }
  }

  // removes the given edge from the outEdges list
  public void removeEdge(Edge e) {
    int eIndex = outEdges.indexOf(e);
    int eIndexOther = e.to.outEdges.indexOf(e.to.findEdge(e.from));
    outEdges.remove(eIndex);
    e.to.outEdges.remove(eIndexOther);
  }

  // returns the root node of this vertex in the given HashMap
  public Vertex findRoot(HashMap<Vertex, Vertex> hash) {
    if (this.equals(hash.get(this))) {
      return this;
    }
    else {
      return hash.get(this).findRoot(hash);
    }
  }

  // custom equals function
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Vertex)) {
      return false;
    }
    Vertex o = (Vertex) other;
    return this.x == o.x && this.y == o.y && this.left.equals(o.left) && this.top.equals(o.top)
        && this.right.equals(o.right) && this.bottom.equals(o.bottom) && this.color == o.color
        && this.outEdges == o.outEdges;
  }

  // custom hashCode function
  @Override
  public int hashCode() {
    return Integer.toString(x).hashCode() * 1000 + this.y;
  }
}

// to represent the edges connecting two vertexes in a graph
class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    this.weight = new Random().nextInt(1000);
  }

  Edge(Vertex from, Vertex to, boolean rand) {
    this.from = from;
    this.to = to;
    if (rand) {
      this.weight = new Random().nextInt(1000);
    }
    else {
      this.weight = new Random(1).nextInt(1000);
    }
  }

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // returns the other vertex in this edge where the given vertex is from
  public Vertex otherVertex(Vertex other) {
    return this.to.equals(other) ? this.from : this.to;
  }

  // custom equals function
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Edge)) {
      return false;
    }

    Edge o = (Edge) other;
    return this.from.equals(o.from) && this.to.equals(o.to) && this.weight == o.weight;
  }

  // custom hashCode function
  @Override
  public int hashCode() {
    return from.hashCode() + to.hashCode() * 1000 + weight;
  }
}

// to represent a graph with vertices
class Graph {
  int row;
  int col;
  ArrayList<Vertex> allVertices;
  ArrayList<Edge> allEdges;
  ArrayList<Edge> allEdgesTree;
  HashMap<Vertex, Vertex> edges;
  Deque<Vertex> searchedVertices;
  // int cellSize = 1000 / Math.max(row, col);

  Graph(int row, int col) {
    this.row = row;
    this.col = col;
    this.allVertices = new ArrayList<Vertex>();
    this.allEdges = new ArrayList<Edge>();
    this.allEdgesTree = new ArrayList<Edge>();
    this.edges = new HashMap<Vertex, Vertex>();
    this.searchedVertices = new ArrayDeque<Vertex>();
  }

  Graph(int row, int col, ArrayList<Vertex> allVertices) {
    this.row = row;
    this.col = col;
    this.allVertices = allVertices;
    this.allEdges = new ArrayList<Edge>();
    this.allEdgesTree = new ArrayList<Edge>();
    this.edges = new HashMap<Vertex, Vertex>();
    this.searchedVertices = new ArrayDeque<Vertex>();
  }

  // constructs the graph based on the row and col sizes given
  void createGraph() {
    for (int i = 0; i < row; i = i + 1) {
      for (int j = 0; j < col; j = j + 1) {
        allVertices.add(new Vertex(10 + (j * 20), 10 + (i * 20)));
      }
    }
    this.connectAll();
    this.mapAll();
    allVertices.get(0).color = Color.green;
    allVertices.get(allVertices.size() - 1).color = Color.red;
  }

  // creates every edge in the graph and adds them to the list of edges
  void createAllEdges(boolean rand) {
    for (Vertex i : allVertices) {
      for (Vertex j : allVertices) {
        Edge edge = i.createEdge(j, rand);
        if (edge != null && !allEdges.contains(edge)) {
          allEdges.add(edge);
        }
      }
    }
    Collections.sort(allEdges, new WeightComparator());
  }

  // places all the vertices in the list of all vertices on to
  // the given scene
  public WorldScene placeAllVertices(WorldScene scene) {
    for (Vertex i : allVertices) {
      i.place(scene);
    }
    Vertex first = allVertices.get(0);
    Vertex last = allVertices.get(allVertices.size() - 1);
    scene.placeImageXY(first.drawVertex(Color.green), first.x, first.y);
    scene.placeImageXY(last.drawVertex(Color.red), last.x, last.y);
    return scene;
  }

  // creates all the connections between vertices in the list of vertices
  void connectAll() {
    for (Vertex i : allVertices) {
      for (Vertex j : allVertices) {
        i.connect(j);
      }
    }
  }

  // maps every vertex to itself in the edges HashMap
  void mapAll() {
    for (Vertex i : allVertices) {
      edges.put(i, i);
    }
  }

  // creates a spanning tree of edges by connecting edges based on their
  // weight and removing edges that create cycles
  public void kruskalsAlgo() {
    while (allEdges.size() > 0) {
      Edge edge = allEdges.remove(0);
      if (edge.from.findRoot(edges).equals(edge.to.findRoot(edges))) {
        edge.from.removeEdge(edge);
      }
      else {
        allEdgesTree.add(edge);
        edges.replace(edge.from.findRoot(edges), edge.to.findRoot(edges));
      }
    }
  }

  // performs a breadth-first search on this graph
  public void bfs(Vertex from, Vertex to) {
    search(from, to, new Queue<Vertex>());
  }

  // performs a depth-first search on this graph
  public void dfs(Vertex from, Vertex to) {
    search(from, to, new Stack<Vertex>());
  }

  // performs the given type of search on this graph
  public void search(Vertex from, Vertex to, ACollection<Vertex> worklist) {
    ArrayList<Vertex> alreadySeen = new ArrayList<Vertex>();
    HashMap<Vertex, Edge> cameFromEdge = new HashMap<Vertex, Edge>();

    worklist.add(from);
    while (!worklist.isEmpty()) {
      Vertex next = worklist.remove();
      if (next.equals(to)) {
        searchedVertices.addLast(next);
        reconstruct(cameFromEdge, next);
        break;
      }
      else if (alreadySeen.contains(next)) {
        continue;
      }
      else {
        alreadySeen.add(next);
        for (Edge e : next.outEdges) {
          Vertex vertex = e.otherVertex(next);
          worklist.add(vertex);
          if (!alreadySeen.contains(vertex)) {
            cameFromEdge.put(vertex, new Edge(next, vertex));
          }
        }
        if (!searchedVertices.contains(next)) {
          searchedVertices.addLast(next);
        }
      }
    }
  }

  // traces the path of the original search to get from the end of the maze
  // to the beginning, recoloring vertices as it goes
  public void reconstruct(HashMap<Vertex, Edge> seenEdges, Vertex source) {
    Edge edge = seenEdges.get(source);
    if (edge != null) {
      searchedVertices.addLast(edge.to);
      this.reconstruct(seenEdges, edge.from);
    }
  }
}

// compares two edges based on their weight
class WeightComparator implements Comparator<Edge> {
  // compares the weight of the two given edges
  public int compare(Edge e1, Edge e2) {
    if (e1.weight == e2.weight) {
      return 0;
    }
    else if (e1.weight > e2.weight) {
      return 1;
    }
    else {
      return -1;
    }
  }
}

// represents an iterator used to visualize the search for the exit
// by coloring searched nodes one at a time
class MazeIterator<T> implements Iterator<Vertex> {
  Deque<Vertex> worklist;
  Deque<Vertex> backtrack;

  MazeIterator(Deque<Vertex> worklist) {
    this.worklist = worklist;
    this.backtrack = new ArrayDeque<Vertex>();
  }

  // returns true if the next element to be iterated in the worklist
  // is in the backtrack deque, meaning it is part of the correct path from
  // reconstruct
  public boolean correctPath() {
    if (this.hasNext()) {
      return backtrack.contains(worklist.getFirst());
    }
    else {
      return false;
    }
  }

  // returns true if the worklist has at least one element
  public boolean hasNext() {
    return this.worklist.size() > 0;
  }

  // removes the first item in the worklist and returns it
  public Vertex next() {
    Vertex vertex = this.worklist.pop();
    backtrack.add(vertex);
    return vertex;
  }
}

// represents the world class for the Maze
class MazeWorld extends World {
  Graph graph;
  MazeIterator<Vertex> iterator;
  WorldScene maze;
  boolean gameOver;

  MazeWorld(Graph graph) {
    this.graph = graph;
    this.iterator = null;
    WorldScene scene = new WorldScene(20 * graph.col, 20 * graph.row);
    this.maze = this.addVerticesToScene(scene);
    this.gameOver = false;
  }

  // draws the game on to a scene
  @Override
  public WorldScene makeScene() {
    return maze;
  }

  // adds the list of vertices to the scene
  WorldScene addVerticesToScene(WorldScene scene) {
    return graph.placeAllVertices(scene);
  }

  // calls a set of methods every tick
  @Override
  public void onTick() {
    this.visualizeSearch();
  }

  // changes the color of searched vertices to visualize the search
  // going through the maze
  public void visualizeSearch() {
    if (iterator != null) {
      if (iterator.hasNext() && iterator.correctPath()) {
        Vertex vertex = iterator.next();
        maze.placeImageXY(vertex.drawVertex(Color.blue), vertex.x, vertex.y);
      }
      else if (iterator.hasNext()) {
        Vertex vertex = iterator.next();
        maze.placeImageXY(vertex.drawVertex(Color.cyan), vertex.x, vertex.y);
      }
      else {
        Vertex first = graph.allVertices.get(0);
        maze.placeImageXY(first.drawVertex(Color.blue), first.x, first.y);
        iterator = null;
        gameOver = true;
      }
    }
  }

  // performs a breadth-first search on the graph of this world if b is pressed
  // and performs a depth-first search on the graph of this world if d is pressed
  public void onKeyEvent(String key) {
    if (key.equals("b")) {
      graph.bfs(graph.allVertices.get(0), graph.allVertices.get(graph.allVertices.size() - 1));
      iterator = new MazeIterator<Vertex>(graph.searchedVertices);
    }
    else if (key.equals("d")) {
      graph.dfs(graph.allVertices.get(0), graph.allVertices.get(graph.allVertices.size() - 1));
      iterator = new MazeIterator<Vertex>(graph.searchedVertices);
    }
  }

  // ends the world function
  @Override
  public WorldEnd worldEnds() {
    if (gameOver) {
      return new WorldEnd(true, this.makeEndScene2());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // returns the endScene WorldScene
  public WorldScene makeEndScene() {
    WorldScene endScene = maze;
    return endScene;
  }

  public WorldScene makeEndScene2() {
    WorldScene endScene = maze;
    return endScene;
  }
}

class ExamplesMaze {
  void testBigBang(Tester t) {
    Graph graph = new Graph(40, 60);
    graph.createGraph();
    graph.createAllEdges(true);
    graph.kruskalsAlgo();
    MazeWorld world = new MazeWorld(graph);
    world.bigBang(20 * graph.col, 20 * graph.row, 1.0 / 28.0);
  }

  void initData() {
    vertex1 = new Vertex(20, 20);
    vertex2 = new Vertex(40, 20);
    vertex3 = new Vertex(0, 20);
    vertex4 = new Vertex(20, 0);
    vertex5 = new Vertex(20, 40);

    vertex6 = new Vertex(200, 200);
    vertex7 = new Vertex(80, 80);

    edge1 = new Edge(vertex1, vertex2, 20);
    edge2 = new Edge(vertex2, vertex1, 25);
    edge3 = new Edge(vertex1, vertex3, 30);
    edge4 = new Edge(vertex3, vertex1, 35);
    edge5 = new Edge(vertex1, vertex4, 40);
    edge6 = new Edge(vertex4, vertex1, 45);
    edge7 = new Edge(vertex1, vertex5, 50);
    edge8 = new Edge(vertex5, vertex1, 55);

    vertex1.outEdges.add(edge1);
    vertex2.outEdges.add(edge2);
    vertex1.outEdges.add(edge3);
    vertex3.outEdges.add(edge4);
    vertex1.outEdges.add(edge5);
    vertex4.outEdges.add(edge6);
    vertex1.outEdges.add(edge7);
    vertex5.outEdges.add(edge8);

    testList = new ArrayList<Vertex>(Arrays.asList(vertex1, vertex2, vertex3, vertex4, vertex5));

    testGraph = new Graph(3, 3, testList);
    testWorld = new MazeWorld(testGraph);

    graphVertex1 = new Vertex(10, 10);
    graphVertex2 = new Vertex(30, 10);
    graphVertex3 = new Vertex(50, 10);
    graphVertex4 = new Vertex(50, 30);
    graphVertex5 = new Vertex(30, 30);
    graphVertex6 = new Vertex(10, 30);
    graphVertex7 = new Vertex(10, 50);
    graphVertex8 = new Vertex(30, 50);
    graphVertex9 = new Vertex(50, 50);

    testList2 = new ArrayList<Vertex>(Arrays.asList(graphVertex1, graphVertex2, graphVertex3,
        graphVertex4, graphVertex5, graphVertex6, graphVertex7, graphVertex8, graphVertex9));

    testGraph2 = new Graph(3, 3, testList2);
    testGraph2.connectAll();
    testGraph2.mapAll();
    graphVertex1.color = Color.green;
    graphVertex9.color = Color.red;

    testWorld2 = new MazeWorld(testGraph2);
  }

  Vertex vertex1;
  Vertex vertex2;
  Vertex vertex3;
  Vertex vertex4;
  Vertex vertex5;

  Vertex vertex6;
  Vertex vertex7;

  Edge edge1;
  Edge edge2;
  Edge edge3;
  Edge edge4;
  Edge edge5;
  Edge edge6;
  Edge edge7;
  Edge edge8;

  ArrayList<Vertex> testList;
  Graph testGraph;
  MazeWorld testWorld;

  Vertex graphVertex1;
  Vertex graphVertex2;
  Vertex graphVertex3;
  Vertex graphVertex4;
  Vertex graphVertex5;
  Vertex graphVertex6;
  Vertex graphVertex7;
  Vertex graphVertex8;
  Vertex graphVertex9;

  ArrayList<Vertex> testList2;

  Graph testGraph2;
  MazeWorld testWorld2;

  void testConnect(Tester t) {
    this.initData();

    vertex1.connect(vertex2);
    vertex1.connect(vertex3);
    vertex1.connect(vertex4);
    vertex1.connect(vertex5);
    t.checkExpect(vertex1.left, vertex3);
    t.checkExpect(vertex1.top, vertex4);
    t.checkExpect(vertex1.right, vertex2);
    t.checkExpect(vertex1.bottom, vertex5);
    t.checkExpect(vertex2.right, null);
    t.checkExpect(vertex3.left, null);
    t.checkExpect(vertex4.top, null);
    t.checkExpect(vertex5.bottom, null);
  }

  boolean testConnectAll(Tester t) {
    this.initData();
    ArrayList<Vertex> connectList = new ArrayList<Vertex>();
    connectList.add(vertex1);
    connectList.add(vertex2);
    connectList.add(vertex3);
    connectList.add(vertex4);
    connectList.add(vertex5);

    vertex1.connect(vertex2);
    vertex1.connect(vertex3);
    vertex1.connect(vertex4);
    vertex1.connect(vertex5);

    vertex2.connect(vertex3);
    vertex2.connect(vertex4);
    vertex2.connect(vertex5);

    vertex3.connect(vertex4);
    vertex3.connect(vertex5);

    vertex4.connect(vertex5);

    testGraph.connectAll();
    return t.checkExpect(testList, connectList);
  }

  void testCreateEdge(Tester t) {
    this.initData();
    Edge testEdge1 = new Edge(graphVertex1, graphVertex2, 985);
    Edge testEdge2 = new Edge(graphVertex2, graphVertex3, 985);
    t.checkExpect(graphVertex1.createEdge(graphVertex2, false), testEdge1);
    t.checkExpect(graphVertex2.createEdge(graphVertex3, false), testEdge2);
    t.checkExpect(graphVertex1.createEdge(graphVertex7, false), null);
  }

  void testCreateAllEdges(Tester t) {
    this.initData();
    testGraph2.createAllEdges(true);
    t.checkExpect(testGraph2.allEdges.size() > 0, true);
    t.checkExpect(testGraph2.allEdgesTree.size() == 0, true);
    t.checkExpect(testGraph2.edges.size() > 0, true);
  }

  void testFindEdge(Tester t) {
    this.initData();
    t.checkExpect(vertex1.findEdge(vertex2), edge1);
    t.checkExpect(vertex1.findEdge(vertex3), edge3);
    t.checkExpect(vertex1.findEdge(vertex4), edge5);
    t.checkExpect(vertex1.findEdge(vertex5), edge7);
    t.checkExpect(vertex1.findEdge(vertex1), null);
    t.checkExpect(vertex1.findEdge(vertex6), null);
    t.checkExpect(vertex2.findEdge(vertex1), edge2);
  }

  void testRemoveEdge(Tester t) {
    this.initData();
    t.checkExpect(vertex1.findEdge(vertex2), edge1);
    vertex1.removeEdge(edge1);
    t.checkExpect(vertex1.findEdge(vertex2), null);

    t.checkExpect(vertex1.findEdge(vertex3), edge3);
    vertex1.removeEdge(edge3);
    t.checkExpect(vertex1.findEdge(vertex3), null);

    t.checkExpect(vertex1.findEdge(vertex4), edge5);
    vertex1.removeEdge(edge5);
    t.checkExpect(vertex1.findEdge(vertex4), null);

    t.checkExpect(vertex1.findEdge(vertex5), edge7);
    vertex1.removeEdge(edge7);
    t.checkExpect(vertex1.findEdge(vertex5), null);
  }

  void testOtherVertex(Tester t) {
    this.initData();
    t.checkExpect(edge1.otherVertex(vertex1), vertex2);
    t.checkExpect(edge2.otherVertex(vertex2), vertex1);
    t.checkExpect(edge3.otherVertex(vertex1), vertex3);
    t.checkExpect(edge4.otherVertex(vertex3), vertex1);
    t.checkExpect(edge5.otherVertex(vertex1), vertex4);
    t.checkExpect(edge6.otherVertex(vertex4), vertex1);
    t.checkExpect(edge7.otherVertex(vertex1), vertex5);
    t.checkExpect(edge8.otherVertex(vertex5), vertex1);
  }

  void testMapAll(Tester t) {
    this.initData();
    testGraph.mapAll();
    t.checkExpect(testGraph.edges.get(vertex1), vertex1);
    t.checkExpect(testGraph.edges.get(vertex2), vertex2);
    t.checkExpect(testGraph.edges.get(vertex3), vertex3);
    t.checkExpect(testGraph.edges.get(vertex4), vertex4);
    t.checkExpect(testGraph.edges.get(vertex5), vertex5);
    t.checkExpect(testGraph.edges.get(vertex6), null);
    t.checkExpect(testGraph.edges.get(vertex7), null);
  }

  void testCreateGraph(Tester t) {
    this.initData();
    Graph graph = new Graph(3, 3);
    graph.createGraph();
    t.checkExpect(graph.allVertices.get(0), graphVertex1);
    t.checkExpect(graph.allVertices.get(1), graphVertex2);
    t.checkExpect(graph.allVertices.get(2), graphVertex3);
    t.checkExpect(graph.allVertices.get(3), graphVertex6);
    t.checkExpect(graph.allVertices.get(4), graphVertex5);
    t.checkExpect(graph.allVertices.get(5), graphVertex4);
    t.checkExpect(graph.allVertices.get(6), graphVertex7);
    t.checkExpect(graph.allVertices.get(7), graphVertex8);
    t.checkExpect(graph.allVertices.get(8), graphVertex9);
  }

  void testKruskalsAlgo(Tester t) {
    this.initData();
    testGraph2.createAllEdges(true);
    testGraph2.kruskalsAlgo();
    t.checkExpect(testGraph2.allEdges.size() == 0, true);
    t.checkExpect(testGraph2.allEdgesTree.size() > 0, true);
  }

  void testBFS(Tester t) {
    this.initData();
    testGraph2.bfs(testGraph2.allVertices.get(0),
        testGraph2.allVertices.get(testGraph2.allVertices.size() - 1));
    t.checkExpect(testGraph2.searchedVertices.size() > 0, true);
  }

  void testDFS(Tester t) {
    this.initData();
    testGraph2.dfs(testGraph2.allVertices.get(0),
        testGraph2.allVertices.get(testGraph2.allVertices.size() - 1));
    t.checkExpect(testGraph2.searchedVertices.size() > 0, true);
  }

  void testSearch(Tester t) {
    this.initData();
    testGraph.search(testGraph.allVertices.get(0),
        testGraph.allVertices.get(testGraph.allVertices.size() - 1), new Queue<Vertex>());
    testGraph2.search(testGraph2.allVertices.get(0),
        testGraph2.allVertices.get(testGraph2.allVertices.size() - 1), new Stack<Vertex>());
    t.checkExpect(testGraph.searchedVertices.size() > 0, true);
    t.checkExpect(testGraph2.searchedVertices.size() > 0, true);
  }

  void testReconstruct(Tester t) {
    this.initData();
    Vertex first = testGraph2.allVertices.get(0);
    Vertex last = testGraph2.allVertices.get(testGraph2.allVertices.size() - 1);
    testGraph2.dfs(first, last);
    t.checkExpect(testGraph2.searchedVertices.contains(last), false);
    t.checkExpect(testGraph2.searchedVertices.contains(first), true);
  }

  void testWorldEnds(Tester t) {
    this.initData();
    testWorld2.gameOver = true;
    t.checkExpect(testWorld2.worldEnds(), new WorldEnd(true, testWorld2.makeEndScene()));
    t.checkExpect(testWorld.worldEnds(), new WorldEnd(false, testWorld.makeEndScene()));
  }

  void testCompare(Tester t) {
    this.initData();
    Comparator<Edge> comp = new WeightComparator();
    t.checkExpect(comp.compare(edge1, edge1), 0);
    t.checkExpect(comp.compare(edge1, edge2), -1);
    t.checkExpect(comp.compare(edge2, edge1), 1);
    t.checkExpect(comp.compare(edge3, edge4), -1);
    t.checkExpect(comp.compare(edge4, edge2), 1);
    t.checkExpect(comp.compare(edge3, edge3), 0);
  }

  void testIterator(Tester t) {
    this.initData();
    Deque<Vertex> searched = testGraph2.searchedVertices;
    searched.addLast(graphVertex1);
    searched.addLast(graphVertex2);
    searched.addLast(graphVertex3);
    searched.addLast(graphVertex4);
    searched.addLast(graphVertex5);
    searched.addLast(graphVertex6);
    searched.addLast(graphVertex7);
    searched.addLast(graphVertex8);
    searched.addLast(graphVertex9);
    searched.addLast(graphVertex1);

    MazeIterator<Vertex> iter = new MazeIterator<Vertex>(searched);
    MazeIterator<Vertex> emptyIter = new MazeIterator<Vertex>(new ArrayDeque<Vertex>());

    t.checkExpect(emptyIter.hasNext(), false);
    t.checkExpect(emptyIter.correctPath(), false);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex1);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex2);
    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex3);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex4);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex5);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex6);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex7);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex8);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), false);
    t.checkExpect(iter.next(), graphVertex9);

    t.checkExpect(iter.hasNext(), true);
    t.checkExpect(iter.correctPath(), true);
    t.checkExpect(iter.next(), graphVertex1);
  }
}
