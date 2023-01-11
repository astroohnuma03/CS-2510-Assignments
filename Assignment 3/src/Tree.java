import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import javalib.worldcanvas.WorldCanvas;

import java.awt.Color;

interface ITree {

  // draws the images on to a canvas
  WorldImage draw();

  // checks if any of the stems or branches in an ITree are drooping
  boolean isDrooping();

  // combines two ITree images
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);

  // rotates all the images in this ITree
  ITree combineHelper(double theta);

  // finds the width of an image by combining the width of the left and right
  // sides of the image
  double getWidth();

  // finds the width of the left side of the image
  double getLeftWidth();

  // finds the width of the right side of the image
  double getRightWidth();
}

class Leaf implements ITree {
  /*
   * TEMPLATE
   * Fields:
   * this.size ... int
   * this.color ... Color
   * Methods:
   * this.draw() ... WorldImage
   * this.isDrooping() ... boolean
   * this.combine(int leftLength, int rightLength,
   *  double leftTheta, double rightTheta,) ... ITree
   * this.combineHelper(double) ... ITree
   * this.getWidth() ... double
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   * Methods on Fields:
   * this.combineHelper(double) ... ITree
   * otherTree.combineHelper(double) ... ITree
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   */
  
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  // draws the leaf to the canvas
  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  // returns false as if isDrooping calls a leaf,
  // it has reached the end of the tree and found no errors
  public boolean isDrooping() {
    return false;
  }
  
  // returns the combined version of this image
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.combineHelper(leftTheta),
        otherTree.combineHelper(rightTheta));
  }
  
  // returns the leaf normally as the stem and branch control rotation
  public ITree combineHelper(double theta) {
    return this;
  }
  
  // returns the width of the leaf by adding the left and right width
  public double getWidth() {
    return this.getRightWidth() - this.getLeftWidth();
  }
  
  // returns the radius of the leaf
  public double getRightWidth() {
    return this.size;
  }
  
  // returns the negative radius of the leaf
  public double getLeftWidth() {
    return -this.size;
  }
}

class Stem implements ITree {
  /*
   * TEMPLATE
   * Fields:
   * this.length ... int
   * this.theta ... double
   * this.tree ... ITree
   * Methods:
   * this.draw() ... WorldImage
   * this.isDrooping() ... boolean
   * this.combine(int leftLength, int rightLength,
   *  double leftTheta, double rightTheta,) ... ITree
   * this.combineHelper(double) ... ITree
   * this.getWidth() ... double
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   * Methods on Fields:
   * this.tree.draw() ... WorldImage
   * this.tree.isDrooping() ... boolean
   * this.combineHelper(double - 90) ... ITree
   * otherTree.combineHelper(double - 90) ... ITree
   * this.tree.combineHelper(double) ... ITree
   * this.tree.getWidth() ... double
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   * tree.getLeftWidth() ... double
   * tree.getRightWidth() ... double
   */
  
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }

  // draws the stem to a canvas
  public WorldImage draw() {
    int x = (int) (this.length * Math.cos(Math.toRadians(180 - theta)));
    int y = (int) (this.length * Math.sin(Math.toRadians(180 - theta)));
    WorldImage stem = new VisiblePinholeImage(
        new LineImage(new Posn(x, y), Color.BLACK).movePinhole((x / -2), (y / -2)));

    return new VisiblePinholeImage(new OverlayImage(stem, this.tree.draw()).movePinhole(x, y));
  }
  
  // returns true if this stem is drooping
  public boolean isDrooping() {
    if (this.theta < 0 && this.theta > -180 || this.theta > 180 && this.theta < 360) {
      return true;
    }
    else {
      return this.tree.isDrooping();
    }
  }
  
  // creates a new branch by combining this stem and another ITree
  // with their angles shifted
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.combineHelper(leftTheta - 90), otherTree.combineHelper(rightTheta - 90));
  }
  
  // rotates the stem in accordance with the angle of the branch the stem
  // is attached to
  public ITree combineHelper(double theta) {
    return new Stem(this.length, (this.theta + theta), this.tree.combineHelper(theta));
  }
  
  // calculates the total width of stem by adding the widths of the
  // left and right sides or returns the width of tree if the angle is 90
  public double getWidth() {
    if (this.theta == 90) {
      return this.tree.getWidth();
    }
    else {
      return this.getRightWidth() + this.getLeftWidth();
    }
  }
  
  // returns the width of the stem if it is left leaning, returns 0
  // otherwise
  public double getLeftWidth() {
    if (this.theta > 90 && this.theta < 270) {
      return (this.length * Math.cos(Math.toRadians(theta))) + tree.getLeftWidth();
    }
    else {
      return 0;
    }
  }
  
  // returns the width of the stem if it is right leaning, returns 0
  // otherwise
  public double getRightWidth() {
    if (this.theta < 90 && this.theta > -90) {
      return (this.length * Math.cos(Math.toRadians(theta))) + tree.getRightWidth();
    }
    else {
      return 0;
    }
  }
}

class Branch implements ITree {
  /*
   * TEMPLATE
   * Fields:
   * this.leftLength ... int
   * this.rightLength ... int
   * this.leftTheta ... double
   * this.rightTheta ... double
   * this.left ... ITree
   * this.right ... ITree
   * Methods:
   * this.draw() ... WorldImage
   * this.isDrooping() ... boolean
   * this.combine(int leftLength, int rightLength,
   *  double leftTheta, double rightTheta,) ... ITree
   * this.combineHelper(double) ... ITree
   * this.getWidth() ... double
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   * Methods on Fields:
   * this.left.draw() ... WorldImage
   * this.right.draw() ... WorldImage
   * this.left.isDrooping() ... boolean
   * this.right.isDrooping() ... boolean
   * this.combineHelper(double - 90) ... ITree
   * otherTree.combineHelper(double - 90) ... ITree
   * this.left.combineHelper(double) ... ITree
   * this.right.combineHelper(double) ... ITree
   * this.tree.getWidth() ... double
   * this.getRightWidth() ... double
   * this.getLeftWidth() ... double
   * left.getLeftWidth() ... double
   * right.getRightWidth() ... double
   */
  
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }
  
  // draws the branch on a canvas
  public WorldImage draw() {
    int x = (int) (this.leftLength * Math.cos(Math.toRadians(180 - leftTheta)));
    int y = (int) (this.leftLength * Math.sin(Math.toRadians(180 - leftTheta)));
    int x2 = (int) (this.rightLength * Math.cos(Math.toRadians(180 - rightTheta)));
    int y2 = (int) (this.rightLength * Math.sin(Math.toRadians(180 - rightTheta)));
    WorldImage stem1 = new VisiblePinholeImage(
        new LineImage(new Posn(x, y), Color.BLACK).movePinhole((x / -2), (y / -2)));
    WorldImage stem2 = new VisiblePinholeImage(
        new LineImage(new Posn(x2, y2), Color.BLACK).movePinhole((x2 / -2), (y2 / -2)));

    WorldImage branch1 = new VisiblePinholeImage(
        new OverlayImage(stem1, this.left.draw()).movePinhole(x, y));
    WorldImage branch2 = new VisiblePinholeImage(
        new OverlayImage(stem2, this.right.draw()).movePinhole(x2, y2));

    return new OverlayImage(branch1, branch2);
  }
  
  // returns true if the stems of the branch are drooping, otherwise
  // recursively checks if the ITrees attached to it are drooping
  public boolean isDrooping() {
    if (this.leftTheta < 0 && this.leftTheta > -180 || this.leftTheta > 180 && this.leftTheta < 360
        || this.rightTheta < 0 && this.rightTheta > -180
        || this.rightTheta > 180 && this.rightTheta < 360) {
      return true;
    }
    else {
      return this.left.isDrooping() || this.right.isDrooping();
    }
  }
  
  // returns a new branch which combines this branch and another ITree,
  // rotating them to align with this branch
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.combineHelper(leftTheta - 90), otherTree.combineHelper(rightTheta - 90));
  }
  
  // rotates the branch according to the angle of the combined branch
  public ITree combineHelper(double theta) {
    return new Branch(leftLength, rightLength, (leftTheta + theta), (rightTheta + theta),
        this.left.combineHelper(theta), this.right.combineHelper(theta));
  }
  
  // returns the total width of this branch by adding the width
  // of the left and right sides
  public double getWidth() {
    return this.getRightWidth() + this.getLeftWidth();
  }
  
  // returns the width of the left side of the branch
  public double getLeftWidth() {
    return (this.leftLength * Math.cos(Math.toRadians(leftTheta))) + left.getLeftWidth();
  }
  
  // returns the width of the right side of the branch
  public double getRightWidth() {
    return (this.rightLength * Math.cos(Math.toRadians(rightTheta))) + right.getRightWidth();
  }
}

class ExamplesTree {
  // examples of leaves
  ITree leaf1 = new Leaf(10, Color.RED);
  ITree leaf2 = new Leaf(15, Color.BLUE);
  ITree leaf3 = new Leaf(15, Color.GREEN);
  ITree leaf4 = new Leaf(8, Color.ORANGE);

  // examples of branches
  ITree tree1 = new Branch(30, 30, 135, 40, this.leaf1, this.leaf2);
  ITree tree2 = new Branch(30, 30, 115, 65, this.leaf3, this.leaf4);
  ITree tree3 = new Branch(40, 50, 150, 30, tree1, tree2);
  ITree tree4 = new Branch(40, 50, -40, 250, this.leaf1, this.leaf2);
  ITree tree5 = new Branch(30, 30, 225, 130, this.leaf1, this.leaf2);

  // examples of stems
  ITree stem1 = new Stem(40, 90, tree1);
  ITree stem2 = new Stem(50, 90, tree2);
  ITree stem3 = new Stem(50, 90, leaf1);
  ITree stem4 = new Stem(30, 150, leaf2);
  ITree stem5 = new Stem(30, 225, leaf1);

  // test drawing a normal stem
  boolean testDrawStem(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(stem1.draw(), 250, 250)) && c.show();
  }
  
  // test drawing a far-leaning stem
  boolean testDrawStem2(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(stem4.draw(), 250, 250)) && c.show();
  }
  
  // test drawing a normal branch
  boolean testDrawBranch(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(tree1.draw(), 250, 250)) && c.show();
  }
  
  // test drawing a more complex branch
  boolean testDrawBranch2(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(tree3.draw(), 250, 250)) && c.show();
  }
  
  // test to check if an ITree is drooping
  boolean testIsDrooping(Tester t) {
    return t.checkExpect(stem5.isDrooping(), true) && t.checkExpect(tree3.isDrooping(), false)
        && t.checkExpect(tree4.isDrooping(), true);
  }
  
  // test drawing a combined branch
  boolean testDrawCombine(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(tree1.combine(40, 50, 150, 30, tree2).draw(), 250, 250))
        && c.show();
  }
  
  // test finding the width of an ITree
  boolean testGetWidth(Tester t) {
    return t.checkExpect(leaf1.getWidth(), leaf1.draw().getWidth())
        && t.checkExpect(stem3.getWidth(), stem3.draw().getWidth())
        && t.checkExpect(tree1.getWidth(), tree1.draw().getWidth());
  }
}