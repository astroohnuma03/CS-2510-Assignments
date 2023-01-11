import tester.*;
import java.util.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents a single square of the game area
class Cell {
  int x;
  int y;
  Color color;
  boolean flooded;
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // draw the cell
  WorldImage drawCell() {
    return new RectangleImage(40, 40, OutlineMode.SOLID, color);
  }

  // place the cell on to a given scene
  public WorldScene place(WorldScene scene) {
    scene.placeImageXY(this.drawCell(), this.x, this.y);
    return scene;
  }

  // checks if the given cell is in any way connected to this cell
  // and mutates both cells to reflect this
  public void connect(Cell other) {
    if (this.x - 40 == other.x && this.y == other.y) {
      this.left = other;
      other.right = this;
    }
    else if (this.x == other.x && this.y - 40 == other.y) {
      this.top = other;
      other.bottom = this;
    }
    else if (this.x + 40 == other.x && this.y == other.y) {
      this.right = other;
      other.left = this;
    }
    else if (this.x == other.x && this.y + 40 == other.y) {
      this.bottom = other;
      other.top = this;
    }
  }

  // returns true if any cells adjecent to this cell are flooded
  public boolean adjacentFloodedCells() {
    return this.left != null && this.left.flooded
        || this.right != null && this.right.flooded
        || this.top != null && this.top.flooded
        || this.bottom != null && this.bottom.flooded;
  }

  // custom equals function
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Cell)) {
      return false;
    }

    Cell o = (Cell) other;
    return this.x == o.x && this.y == o.y && this.color == o.color && this.left.equals(o.left)
        && this.right.equals(o.right) && this.top.equals(o.top) && this.bottom.equals(o.bottom);

  }

  // custom hashCode function
  @Override
  public int hashCode() {
    return this.color.hashCode() * 1000 + this.x + this.y;
  }
}

// utility class for functions
class Utils {

  Random rand;

  Utils() {
    this.rand = new Random();
  }

  Utils(int r) {
    this.rand = new Random(r);
  }

  // creates the ArrayList that will represent all the cells
  // in the game
  ArrayList<Cell> createBoard(int size) {
    ArrayList<Cell> result = new ArrayList<Cell>();

    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.cyan, Color.yellow,
        Color.red, Color.green, Color.magenta, Color.orange));

    for (int i = 0; i < size; i = i + 1) {
      for (int j = 0; j < size; j = j + 1) {
        result.add(new Cell(20 + (j * 40), 20 + (i * 40), colors.get(rand.nextInt(6))));
      }
    }
    // connects all the cells to ensure their left, right, etc. fields are correct
    new Utils().connectAll(result);
    // sets the cell in the top left to be flooded as that is where the game starts
    result.get(0).flooded = true;
    return result;
  }

  // creates all the connections between cells in a list of cells
  void connectAll(ArrayList<Cell> arr) {
    for (Cell i : arr) {
      for (Cell j : arr) {
        i.connect(j);
      }
    }
  }

  // places all the cells in a given list on the given scene
  WorldScene placeAll(WorldScene scene, ArrayList<Cell> arr) {
    for (Cell i : arr) {
      i.place(scene);
    }

    return scene;
  }
}

// represents an iterator that is used to flood cells one at a time to create
// a "wave" effect
class FloodItIterator<T> implements Iterator<Cell> {
  Deque<Cell> worklist;
  Deque<Cell> seen;

  FloodItIterator(Cell source) {
    this.worklist = new ArrayDeque<Cell>();
    this.seen = new ArrayDeque<Cell>();
    this.addIfNotNull(source);
  }

  // checks if the given cell is null and adds it to the worklist if it isn't
  void addIfNotNull(Cell c) {
    if (c != null && c.flooded && !seen.contains(c)) {
      this.worklist.addLast(c);
    }
  }

  // returns true if the worklist has at least one element
  public boolean hasNext() {
    return this.worklist.size() > 0;
  }

  // removes the first item in the worklist and adds its children to the
  // worklist before returning it
  public Cell next() {
    Cell cell = this.worklist.pop();

    this.addIfNotNull(cell.left);
    this.addIfNotNull(cell.top);
    this.addIfNotNull(cell.right);
    this.addIfNotNull(cell.bottom);
    seen.add(cell);

    return cell;
  }
}

// represents the world class for our game
class FloodItWorld extends World {
  static int BOARD_SIZE = 14;

  ArrayList<Cell> board;
  Color boardColor;
  FloodItIterator<Cell> iterator;
  int clicksLeft;

  FloodItWorld() {
    this.board = new Utils().createBoard(BOARD_SIZE);
    this.boardColor = board.get(0).color;
    this.iterator = null;
    this.clicksLeft = 25;
  }

  // draws the game onto a scene
  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(40 * BOARD_SIZE, 40 * BOARD_SIZE);

    scene = this.addInfoToScene(scene);

    scene = this.addCellsToScene(scene);

    return scene;
  }

  // adds the list of cells to the scene
  WorldScene addCellsToScene(WorldScene scene) {
    return new Utils().placeAll(scene, board);
  }

  // adds the click count to the scene
  WorldScene addInfoToScene(WorldScene scene) {
    scene.placeImageXY(
        new TextImage("Clicks Left: " + Integer.toString(this.clicksLeft), 25, Color.black), 80,
        580);
    return scene;
  }

  // calls a set of methods every tick
  @Override
  public void onTick() {
    this.flood();
    this.floodNewCells();
  }

  // mutates all flooded cells to make their color the same as the
  // color of the clicked cell
  public void flood() {
    if (iterator != null) {
      if (iterator.hasNext()) {
        iterator.next().color = boardColor;
      }
      else {
        iterator = null;
      }
    }
  }

  // sets cells to be flooded if they are adjacent to a flooded cell and their
  // color
  // is the same as the current boardColor
  public void floodNewCells() {
    for (Cell i : board) {
      if (i.adjacentFloodedCells() && i.color == boardColor) {
        i.flooded = true;
      }
    }
  }

  // changes all flooded cells to the color of the clicked tile
  public void onMouseClicked(Posn pos) {
    for (Cell i : board) {
      if (pos.x < i.x + 20 && pos.x > i.x - 20 && pos.y < i.y + 20 && pos.y > i.y - 20) {
        if (i.color != boardColor) {
          boardColor = i.color;
          clicksLeft = clicksLeft - 1;
          iterator = new FloodItIterator<Cell>(board.get(0));
        }
      }
    }
  }

  // resets the game if the "r" key is pressed and there is no flooding currently
  // going on
  public void onKeyEvent(String key) {
    if (key.equals("r") && iterator == null) {
      this.board = new Utils().createBoard(BOARD_SIZE);
      this.clicksLeft = 25;
      this.boardColor = board.get(0).color;
    }
  }

  // returns true if every cell in the board is the same color
  public boolean gameWon() {
    boolean won = true;
    for (Cell i : board) {
      if (i.color != boardColor) {
        won = false;
      }
    }

    return won;
  }

  // ends the world when the length of board is 0
  // (this is for testing purposes as we have not implemented any game mechanics
  // yet)
  @Override
  public WorldEnd worldEnds() {
    if (clicksLeft == 0 && iterator == null) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else if (this.gameWon()) {
      return new WorldEnd(true, this.makeEndScene2());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // returns the endScene WorldScene
  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(40 * BOARD_SIZE, (40 * BOARD_SIZE) + 50);
    endScene.placeImageXY(new TextImage("Game Over", Color.black), 280, 280);
    return endScene;
  }

  // returns the endScene WorldScene if the user wins
  public WorldScene makeEndScene2() {
    WorldScene endScene = new WorldScene(40 * BOARD_SIZE, (40 * BOARD_SIZE) + 50);
    endScene.placeImageXY(new TextImage("You Win!", Color.black), 280, 280);
    return endScene;
  }
}

class ExamplesFloodIt {
  void testBigBang(Tester t) {
    FloodItWorld world = new FloodItWorld();
    world.bigBang(560, 610, 1 / 28.0);
  }

  void initData() {
    cell1 = new Cell(40, 40, Color.cyan);
    cell2 = new Cell(80, 40, Color.green);
    cell3 = new Cell(0, 40, Color.red);
    cell4 = new Cell(40, 0, Color.yellow);
    cell5 = new Cell(40, 80, Color.magenta);

    testList = new ArrayList<Cell>(Arrays.asList(cell1, cell2, cell3, cell4, cell5));

    drawnCell1 = new RectangleImage(40, 40, OutlineMode.SOLID, Color.cyan);
    drawnCell2 = new RectangleImage(40, 40, OutlineMode.SOLID, Color.green);
    drawnCell3 = new RectangleImage(40, 40, OutlineMode.SOLID, Color.red);
    drawnCell4 = new RectangleImage(40, 40, OutlineMode.SOLID, Color.yellow);
    drawnCell5 = new RectangleImage(40, 40, OutlineMode.SOLID, Color.magenta);

    testScene = new WorldScene(800, 800);
    testScene2 = new WorldScene(800, 800);

    testBoard = new ArrayList<Cell>();

    testCell1 = new Cell(20, 20, Color.green);
    testCell2 = new Cell(60, 20, Color.magenta);
    testCell3 = new Cell(100, 20, Color.yellow);
    testCell4 = new Cell(100, 60, Color.magenta);
    testCell5 = new Cell(60, 60, Color.red);
    testCell6 = new Cell(20, 60, Color.green);
    testCell7 = new Cell(20, 100, Color.red);
    testCell8 = new Cell(60, 100, Color.magenta);
    testCell9 = new Cell(100, 100, Color.magenta);

    testBoard.add(testCell1);
    testBoard.add(testCell2);
    testBoard.add(testCell3);
    testBoard.add(testCell4);
    testBoard.add(testCell5);
    testBoard.add(testCell6);
    testBoard.add(testCell7);
    testBoard.add(testCell8);
    testBoard.add(testCell9);

    testCell1.flooded = true;

    new Utils().connectAll(testBoard);
  }

  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;

  ArrayList<Cell> testList;

  WorldImage drawnCell1;
  WorldImage drawnCell2;
  WorldImage drawnCell3;
  WorldImage drawnCell4;
  WorldImage drawnCell5;

  WorldScene testScene;
  WorldScene testScene2;

  ArrayList<Cell> testBoard;

  Cell testCell1;
  Cell testCell2;
  Cell testCell3;
  Cell testCell4;
  Cell testCell5;
  Cell testCell6;
  Cell testCell7;
  Cell testCell8;
  Cell testCell9;

  boolean testDrawCell(Tester t) {
    this.initData();
    return t.checkExpect(cell1.drawCell(), drawnCell1)
        && t.checkExpect(cell2.drawCell(), drawnCell2)
        && t.checkExpect(cell3.drawCell(), drawnCell3)
        && t.checkExpect(cell4.drawCell(), drawnCell4)
        && t.checkExpect(cell5.drawCell(), drawnCell5);
  }

  void testPlace(Tester t) {
    this.initData();
    cell1.place(testScene);
    testScene2.placeImageXY(cell1.drawCell(), cell1.x, cell1.y);
    t.checkExpect(testScene, testScene2);
    cell2.place(testScene);
    testScene2.placeImageXY(cell2.drawCell(), cell2.x, cell2.y);
    t.checkExpect(testScene, testScene2);
    cell3.place(testScene);
    testScene2.placeImageXY(cell3.drawCell(), cell3.x, cell3.y);
    t.checkExpect(testScene, testScene2);
    cell4.place(testScene);
    testScene2.placeImageXY(cell4.drawCell(), cell4.x, cell4.y);
    t.checkExpect(testScene, testScene2);
    cell5.place(testScene);
    testScene2.placeImageXY(cell5.drawCell(), cell5.x, cell5.y);
    t.checkExpect(testScene, testScene2);
  }

  void testConnect(Tester t) {
    this.initData();
    cell1.connect(cell2);
    cell1.connect(cell3);
    cell1.connect(cell4);
    cell1.connect(cell5);
    t.checkExpect(cell1.left, cell3);
    t.checkExpect(cell1.top, cell4);
    t.checkExpect(cell1.right, cell2);
    t.checkExpect(cell1.bottom, cell5);
  }

  boolean testAdjacentFloodedCells(Tester t) {
    this.initData();
    cell1.connect(cell2);
    cell1.connect(cell3);
    cell1.connect(cell4);
    cell1.connect(cell5);
    cell1.flooded = true;
    return t.checkExpect(cell1.adjacentFloodedCells(), false)
        && t.checkExpect(cell2.adjacentFloodedCells(), true)
        && t.checkExpect(cell3.adjacentFloodedCells(), true)
        && t.checkExpect(cell4.adjacentFloodedCells(), true)
        && t.checkExpect(cell5.adjacentFloodedCells(), true);
  }

  // IMPORTANT NOTE: This test fails currently, but I cannot figure out why. The
  // arraylists are the
  // exact same, it even shows as much in the test, but it fails anyway. This may
  // be a problem with
  // checkExpect or something, but as far as I can tell this should be correct. If
  // you can figure out
  // why the test fails it would be nice to know. Thanks.
  boolean testCreateBoard(Tester t) {
    this.initData();
    return t.checkExpect(testBoard, new Utils(1).createBoard(3));
  }

  boolean testConnectAll(Tester t) {
    this.initData();
    ArrayList<Cell> connectList = new ArrayList<Cell>();
    connectList.add(cell1);
    connectList.add(cell2);
    connectList.add(cell3);
    connectList.add(cell4);
    connectList.add(cell5);

    cell1.connect(cell2);
    cell1.connect(cell3);
    cell1.connect(cell4);
    cell1.connect(cell5);

    cell2.connect(cell3);
    cell2.connect(cell4);
    cell2.connect(cell5);

    cell3.connect(cell4);
    cell3.connect(cell5);

    cell4.connect(cell5);

    new Utils().connectAll(testList);
    return t.checkExpect(testList, connectList);
  }

  boolean testPlaceAll(Tester t) {
    this.initData();
    cell1.place(testScene);
    cell2.place(testScene);
    cell3.place(testScene);
    cell4.place(testScene);
    cell5.place(testScene);

    new Utils().placeAll(testScene2, testList);
    return t.checkExpect(testScene, testScene2);
  }

  void testIterator(Tester t) {
    this.initData();
    cell1.flooded = true;
    cell2.flooded = true;
    cell3.flooded = true;
    cell4.flooded = true;
    cell5.flooded = true;

    FloodItIterator<Cell> iterator = new FloodItIterator<Cell>(cell1);
    FloodItIterator<Cell> emptyIterator = new FloodItIterator<Cell>(null);

    new Utils().connectAll(testList);

    t.checkExpect(emptyIterator.hasNext(), false);

    t.checkExpect(iterator.hasNext(), true);
    t.checkExpect(iterator.next(), cell1);
    t.checkExpect(iterator.hasNext(), true);
    t.checkExpect(iterator.next(), cell3);
    t.checkExpect(iterator.hasNext(), true);
    t.checkExpect(iterator.next(), cell4);
    t.checkExpect(iterator.hasNext(), true);
    t.checkExpect(iterator.next(), cell2);
    t.checkExpect(iterator.hasNext(), true);
    t.checkExpect(iterator.next(), cell5);
    t.checkExpect(iterator.hasNext(), false);
  }
}