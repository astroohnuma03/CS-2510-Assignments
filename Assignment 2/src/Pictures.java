import tester.*;

// to represent a picture
interface IPicture {

  // calculates the width of a given picture
  int getWidth();
  /* TEMPLATE
   * Fields on Parameters:
   * this.size ... int
   * this.operation ... IOperation
   * this.picture ... IPicture
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods on Parameters:
   * this.operation.getWidth() ... int
   * this.picture.getWidth() ... int
   * this.picture1.getWidth() ... int
   * this.picture2.getWidth() ... int
   * this.topPicture.getWidth() ... int
   * this.bottomPicture.getWidth() ... int
   */

  // counts the number of simple shapes used in the picture
  int countShapes();
  /* TEMPLATE
   * Fields on Parameters:
   * this.operation ... IOperation
   * this.picture ... IPicture
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods on Parameters:
   * this.operation.countShapes() ... int
   * this.picture.countShapes() ... int
   * this.picture1.countShapes() ... int
   * this.picture2.countShapes() ... int
   * this.topPicture.countShapes() ... int
   * this.bottomPicture.countShapes() ... int
   */

  // tracks the depth of the operations in a single picture
  int comboDepth();
  /* TEMPLATE
   * Fields on Parameters:
   * this.operation ... IOperation
   * this.picture ... IPicture
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods on Parameters:
   * this.operation.comboDepth() ... int
   * this.picture.comboDepth() ... int
   * this.picture1.comboDepth() ... int
   * this.picture2.comboDepth() ... int
   * this.topPicture.comboDepth() ... int
   * this.bottomPicture.comboDepth() ... int
   */

  // mirrors the images in the Beside operation
  IPicture mirror();
  /* TEMPLATE
   * Fields on Parameters:
   * this.name ... String
   * this.operation ... IOperation
   * Methods on Parameters:
   * this.operation.mirror() ... IOperation
   */

  // creates a string which represents the recipe of the picture and its
  // operations
  String pictureRecipe(int depth);
  /* TEMPLATE
   * Fields on Parameters:
   * this.kind ... String
   * this.name ... String
   * this.operation ... IOperation
   * this.picture ... IPicture
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods on Parameters:
   * this.operation.pictureRecipe(depth) ... String
   * this.picture.pictureRecipe(depth) ... String
   * this.picture1.pictureRecipe(depth - 1) ... String
   * this.picture2.pictureRecipe(depth - 1) ... String
   * this.topPicture.pictureRecipe(depth - 1) ... String
   * this.bottomPicture.pictureRecipe(depth - 1) ... String
   */
}

// to represent a single shape
class Shape implements IPicture {
  /* TEMPLATE
   * Fields:
   * this.kind ... String
   * this.size ... int
   * Methods:
   * this.getWidth() ... int
   * this.countShapes() ... int
   * this.comboDepth() ... int
   * this.mirror() ... IPicture
   * this.pictureRecipe(depth) ... String
   * Methods on Fields:
   * N/A
   */
  
  String kind;
  int size;

  // constructor for a shape
  Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }

  // finds the width of the shape
  public int getWidth() {
    return size;
  }

  // counts the shape
  public int countShapes() {
    return 1;
  }

  // shapes don't use operations so should always return 0
  public int comboDepth() {
    return 0;
  }

  // operation only affects beside
  public IPicture mirror() {
    return this;
  }

  // returns the kind of the shape for the pictureRecipe
  public String pictureRecipe(int depth) {
    return this.kind;
  }
}

// to represent multiple shapes
class Combo implements IPicture {
  /* TEMPLATE
   * Fields:
   * this.name ... String
   * this.operation ... IOperation
   * Methods:
   * this.getWidth() ... int
   * this.countShapes() ... int
   * this.comboDepth() ... int
   * this.mirror() ... IPicture
   * this.pictureRecipe(depth) ... String
   * Methods on Fields:
   * this.operation.getWidth() ... int
   * this.operation.countShapes() ... int
   * this.operation.comboDepth() ... int
   * this.operation.mirror() ... IOperation
   * this.operation.pictureRecipe(depth) ... String
   */
  
  String name;
  IOperation operation;

  // constructor for a combo
  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }

  // finds the overall width of the picture created by the combo
  public int getWidth() {
    return this.operation.getWidth();
  }

  // finds the total number of simple shapes in the operation
  public int countShapes() {
    return this.operation.countShapes();
  }

  // finds the total depth of the operations in the picture
  public int comboDepth() {
    return 1 + this.operation.comboDepth();
  }

  // finds if any of the operations used to create the picture use Beside
  public IPicture mirror() {
    return new Combo(this.name, this.operation.mirror());
  }

  // returns a pictureRecipe based on the given depth
  public String pictureRecipe(int depth) {
    if (depth <= 0) {
      return this.name;
    }
    else {
      return this.operation.pictureRecipe(depth);
    }
  }
}

// to represent operations used in the combo pictures
interface IOperation {
  
  // calculates the width of a given picture
  int getWidth();
  
  // counts the number of simple shapes used in the picture
  int countShapes();
  
  // tracks the depth of the operations in a picture
  int comboDepth();
  
  // mirrors the images in the beside operation
  IOperation mirror();
  /* TEMPLATE
   * Fields on Parameters:
   * this.picture ... IPicture
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods on Parameters:
   * this.picture.mirror() ... IOperation
   * this.picture1.mirror() ... IOperation
   * this.picture2.mirror() ... IOperation
   * this.topPicture.mirror() ... IOperation
   * this.bottomPicture.mirror() ... IOperation
   */
  
  // creates a string which represents the recipe of a picture
  // through its various shapes and combos
  String pictureRecipe(int depth);
}

// to represent an operation that doubles the size of a picture
class Scale implements IOperation {
  /* TEMPLATE
   * Fields:
   * this.picture ... IPicture
   * Methods:
   * this.getWidth() ... int
   * this.countShapes() ... int
   * this.comboDepth() ... int
   * this.mirror() ... IOperation
   * this.pictureRecipe(depth) ... String
   * Methods on Fields:
   * this.picture.getWidth() ... int
   * this.picture.countShapes() ... int
   * this.picture.comboDepth() ... int
   * this.picture.mirror() ... IOperation
   * this.picture.pictureRecipe(depth) ... String
   */
  
  IPicture picture;

  // constructor for scale
  Scale(IPicture picture) {
    this.picture = picture;
  }

  // returns the width of the original shape doubled
  public int getWidth() {
    return this.picture.getWidth() * 2;
  }

  // counts the shape used in the operation
  public int countShapes() {
    return this.picture.countShapes();
  }

  // returns 1 as there is no room for another operation
  public int comboDepth() {
    return this.picture.comboDepth();
  }

  // operation only affects Beside
  public IOperation mirror() {
    return new Scale(this.picture.mirror());
  }

  // returns the description of the scaled shape if depth is 0
  // or returns the simple shape if depth is greater than 0
  public String pictureRecipe(int depth) {
    if (depth <= 0) {
      return "big " + this.picture.pictureRecipe(depth);
    }
    else {
      return "scale(" + this.picture.pictureRecipe(depth - 1) + ")";
    }
  }
}

// to represent an operation that places one picture
// to the left of another picture
class Beside implements IOperation {
  /* TEMPLATE
   * Fields:
   * this.picture1 ... IPicture
   * this.picture2 ... IPicture
   * Methods:
   * this.getWidth() ... int
   * this.countShapes() ... int
   * this.comboDepth() ... int
   * this.mirror() ... IOperation
   * this.pictureRecipe(depth) ... String
   * Methods on Fields:
   * this.picture1.getWidth() ... int
   * this.picture2.getWidth() ... int
   * this.picture1.countShapes() ... int
   * this.picture2.countShapes() ... int
   * this.picture1.comboDepth() ... int
   * this.picture2.comboDepth() ... int
   * this.picture1.mirror() ... IOperation
   * this.picture2.mirror() ... IOperation
   * this.picture1.pictureRecipe(depth) ... String
   * this.picture2.pictureRecipe(depth) ... String
   */
  
  IPicture picture1;
  IPicture picture2;

  // constructor for beside
  Beside(IPicture picture1, IPicture picture2) {
    this.picture1 = picture1;
    this.picture2 = picture2;
  }

  // finds the overall width of the picture by adding
  // the width of the two given pictures together
  public int getWidth() {
    return this.picture1.getWidth() + this.picture2.getWidth();
  }

  // counts the number of simple shapes in both the left and right pictures
  public int countShapes() {
    return this.picture1.countShapes() + this.picture2.countShapes();
  }

  // counts the comboDepth of pictures 1 and 2 and itself
  public int comboDepth() {
    if (this.picture1.comboDepth() >= this.picture2.comboDepth()) {
      return this.picture1.comboDepth();
    }
    else {
      return this.picture2.comboDepth();
    }
  }

  // mirrors the pictures in the Beside operation
  public IOperation mirror() {
    return new Beside(this.picture2.mirror(), this.picture1.mirror());
  }

  // returns the beside operation for the pictureRecipe
  public String pictureRecipe(int depth) {
    return "beside(" + this.picture1.pictureRecipe(depth - 1) + ", "
        + this.picture2.pictureRecipe(depth - 1) + ")";
  }
}

// to represent an operation that places one picture on top
// of another picture with their centers aligned
class Overlay implements IOperation {
  /* TEMPLATE
   * Fields:
   * this.topPicture ... IPicture
   * this.bottomPicture ... IPicture
   * Methods:
   * this.getWidth() ... int
   * this.countShapes() ... int
   * this.comboDepth() ... int
   * this.mirror() ... IOperation
   * this.pictureRecipe(depth) ... String
   * Methods of Fields:
   * this.topPicture.getWidth() ... int
   * this.bottomPicture.getWidth() ... int
   * this.topPicture.countShapes() ... int
   * this.bottomPicture.countShapes() ... int
   * this.topPicture.comboDepth() ... int
   * this.bottomPicture.comboDepth() ... int
   * this.topPicture.mirror() ... IOperation
   * this.bottomPicture.mirror() ... IOperation
   * this.topPicture.pictureRecipe(depth) ... String
   * this.bottomPicture.pictureRecipe(depth) ... String
   */
  
  IPicture topPicture;
  IPicture bottomPicture;

  // constructor for overlay
  Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }

  // finds the overall width of the picture by determining
  // which picture has the greater width
  public int getWidth() {
    if (this.topPicture.getWidth() >= this.bottomPicture.getWidth()) {
      return this.topPicture.getWidth();
    }
    else {
      return this.bottomPicture.getWidth();
    }
  }

  // counts the number of shapes in both the top and bottom pictures
  public int countShapes() {
    return this.topPicture.countShapes() + this.bottomPicture.countShapes();
  }

  // counts the comboDepth of both the top and bottom pictures and itself
  public int comboDepth() {
    if (this.topPicture.comboDepth() >= this.bottomPicture.comboDepth()) {
      return this.topPicture.comboDepth();
    }
    else {
      return this.bottomPicture.comboDepth();
    }
  }

  // finds if either the top or bottom pictures use the Beside operation
  public IOperation mirror() {
    return new Overlay(this.topPicture.mirror(), this.bottomPicture.mirror());
  }

  // returns the overlay operation for the pictureRecipe
  public String pictureRecipe(int depth) {
    return "overlay(" + this.topPicture.pictureRecipe(depth - 1) + ", "
        + this.bottomPicture.pictureRecipe(depth - 1) + ")";
  }
}

// to represent examples of various pictures
class ExamplesPicture {

  // examples representing simple shapes
  IPicture circle = new Shape("circle", 20);
  IPicture square = new Shape("square", 30);

  // examples representing combo's of shapes
  // and their operations
  IPicture bigCircle = new Combo("big circle", new Scale(this.circle));
  IPicture squareOnCircle = new Combo("square on circle", new Overlay(this.square, this.bigCircle));
  IPicture doubledSquareOnCircle = new Combo("doubled square on circle",
      new Beside(this.squareOnCircle, this.squareOnCircle));
  IPicture bigSquare = new Combo("big square", new Scale(this.square));
  IPicture circleOnSquare = new Combo("circle on square", new Overlay(this.circle, this.bigSquare));
  IPicture circleOnSquareSquareOnCircle = new Combo("circle on square and square on circle",
      new Beside(this.circleOnSquare, this.squareOnCircle));

  boolean testGetWidth(Tester t) {
    return t.checkExpect(this.square.getWidth(), 30);
  }

  boolean testGetWidth2(Tester t) {
    return t.checkExpect(this.circleOnSquareSquareOnCircle.getWidth(), 100);
  }

  boolean testCountShapes(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.countShapes(), 4);
  }

  boolean testComboDepth(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.comboDepth(), 3);
  }

  boolean testMirror(Tester t) {
    return t.checkExpect(this.circleOnSquareSquareOnCircle.mirror(),
        new Combo("circle on square and square on circle",
            new Beside(this.squareOnCircle, this.circleOnSquare)));
  }

  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(0), "doubled square on circle");
  }

  boolean testPictureRecipe2(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(2),
        "beside(overlay(square, big circle), overlay(square, big circle))");
  }

  boolean testPictureRecipe3(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(3),
        "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))");
  }
}
