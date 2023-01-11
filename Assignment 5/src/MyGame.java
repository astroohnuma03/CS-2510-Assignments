import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

// represents the posn method except with our own methods applied to it
class MyPosn extends Posn {

  MyPosn(int x, int y) {
    super(x, y);
  }

  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // creates a new MyPosn by adding the x and y values of a given MyPosn
  // to this one
  MyPosn addPosn(MyPosn other) {
    return new MyPosn(this.x + other.x, this.y + other.y);
  }

  // returns true if this MyPosn is off the screen
  boolean isOffScreen(int width, int height) {
    return this.x > width || this.x < 0 || this.y > height || this.y < 0;
  }
}

interface IGamePiece {
  int width = 500;
  int height = 300;
  MyPosn bulletVelocity = new MyPosn(0, -8);
  String bulletColor = "pink";
  int shipRadius = height / 30;
  MyPosn shipVelocity = new MyPosn(8 / 2, 0);
  String shipColor = "cyan";
  String fontColor = "black";
  int fontSize = 13;

  // move this IGamePiece by adding its velocity to its position
  IGamePiece move();

  // returns true if this GamePiece is off screen
  boolean isOffScreenGamePiece(int width, int height);

  // places the GamePiece on a scene
  WorldScene place(WorldScene scene);

  // checks if one GamePiece collides with another
  boolean collidesWith(IGamePiece other);

  // returns the length of a line by finding the difference
  // between two x positions
  double getLengthX(int p);

  // returns the length of a line by finding the difference
  // between two y positions
  double getLengthY(int p);

  // returns the total distance value of the radius of one
  // GamePiece plus the radius of another
  double totalRadius(int other);

  // splits a bullet into multiple bullets when it collides
  // with a ship
  ILoGamePiece explosion();

  // returns a list of bullets created by splitting a bullet
  // that has collided with a ship
  ILoGamePiece explosionHelper(int i);
}

// abstract class for IGamePiece
abstract class AGamePiece implements IGamePiece {
  MyPosn position;
  MyPosn velocity;
  int radius;

  AGamePiece(MyPosn position, MyPosn velocity, int radius) {
    this.position = position;
    this.velocity = velocity;
    this.radius = radius;
  }

  // checks if this position is off the screen
  public boolean isOffScreenGamePiece(int width, int height) {
    return this.position.isOffScreen(width, height);
  }

  // checks if this GamePiece is overlapping with the given
  // GamePiece
  public boolean collidesWith(IGamePiece other) {
    return Math.hypot(other.getLengthX(this.position.x), other.getLengthY(this.position.y)) <= other
        .totalRadius(this.radius);
  }

  // returns the difference between two x positions
  public double getLengthX(int p) {
    return Math.abs(this.position.x - p);
  }

  // returns the difference between two y positions
  public double getLengthY(int p) {
    return Math.abs(this.position.y - p);
  }

  // returns the total radius of this GamePiece and the given
  // GamePiece for collision
  public double totalRadius(int other) {
    return this.radius + other;
  }
}

// represents a Ship GamePiece
class Ship extends AGamePiece {

  Ship(MyPosn position, MyPosn velocity, int radius) {
    super(position, velocity, shipRadius);
  }

  // move this ship's position by its velocity
  public IGamePiece move() {
    return new Ship(this.position.addPosn(this.velocity), this.velocity, this.radius);
  }

  // draw the ship
  WorldImage drawShip() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Color.cyan);
  }

  // place the ship on to the given scene
  public WorldScene place(WorldScene scene) {
    return scene.placeImageXY(this.drawShip(), this.position.x, this.position.y);
  }

  // returns an empty list as explosion shouldn't be called on Ship
  public ILoGamePiece explosion() {
    return new MtLoGamePiece();
  }

  // returns an empty list as explosionHelper shouldn't be called on Ship
  public ILoGamePiece explosionHelper(int i) {
    return new MtLoGamePiece();
  }
}

// represents a Bullet GamePiece
class Bullet extends AGamePiece {
  int n;

  Bullet(MyPosn position, MyPosn velocity, int radius, int n) {
    super(position, velocity, radius);
    this.n = n;
  }

  // move this bullet's position by its velocity
  public IGamePiece move() {
    return new Bullet(this.position.addPosn(this.velocity), this.velocity, this.radius, this.n);
  }

  // draw the bullet
  WorldImage drawBullet() {
    return new CircleImage(this.radius, OutlineMode.SOLID, Color.pink);
  }

  // place the bullet on to a given scene
  public WorldScene place(WorldScene scene) {
    return scene.placeImageXY(this.drawBullet(), this.position.x, this.position.y);
  }

  // calls the explosionHelper method with this.n set as i
  public ILoGamePiece explosion() {
    return this.explosionHelper(n);
  }

  // returns a list of bullets where this bullet collided with a ship
  public ILoGamePiece explosionHelper(int i) {
    int angle = i * (360 / (this.n + 1));
    int x = (int) Math.cos(Math.toRadians(angle));
    int y = (int) Math.sin(Math.toRadians(angle));

    if (i > 0) {
      return new ConsLoGamePiece(
          new Bullet(this.position, new MyPosn(8 * x, 8 * y), this.maxRadius(), this.maxSplit()),
          this.explosionHelper(i - 1));
    }
    else {
      return new ConsLoGamePiece(
          new Bullet(this.position, new MyPosn(8 * x, 8 * y), this.maxRadius(), this.maxSplit()),
          new MtLoGamePiece());
    }
  }

  // checks if n is larger than 6, and if it is sets it to 6
  // otherwise, adds 1 to n
  int maxSplit() {
    if (this.n > 6) {
      return 6;
    }
    else {
      return this.n + 1;
    }
  }

  // checks if the radius of the bullet is greater than 10, and sets it
  // to 10 if it is, otherwise returns the radius plus n
  int maxRadius() {
    if (this.radius + this.maxSplit() > 10) {
      return 10;
    }
    else {
      return this.radius + this.maxSplit();
    }
  }
}

interface ILoGamePiece {

  // move every ship on screen
  ILoGamePiece moveAll();

  // removes GamePieces from a list if they are off screen
  ILoGamePiece removeOffScreen(int width, int height);

  // places a list of ships or bullets on a world scene
  WorldScene placeAll(WorldScene scene);

  // returns the length of the list
  int length(int l);

  // checks if any GamePieces in this list collided with any
  // GamePieces in a given list
  ILoGamePiece collidesWithAll(ILoGamePiece other, boolean collided);

  // checks if a given GamePiece has collided with any of the GamePieces
  // in this list
  boolean collided(IGamePiece other);

  // returns a new list of GamePieces made up of two lists of GamePieces
  // added together
  ILoGamePiece append(ILoGamePiece other);

  // returns the list of all split bullets from a list of destroyed
  // bullets
  ILoGamePiece explodeAll();
}

// represents an empty list of GamePieces
class MtLoGamePiece implements ILoGamePiece {
  MtLoGamePiece() {
  }

  // stop calling moveAll when reaching an empty list element
  public ILoGamePiece moveAll() {
    return this;
  }

  // stop calling removeOffScreen when reaching an empty list element
  public ILoGamePiece removeOffScreen(int width, int height) {
    return this;
  }

  // returns the full scene when reaching the end of the list
  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

  // returns the final int as an empty GamePiece means the end of the list
  public int length(int l) {
    return l;
  }

  // returns the finished list when reaching the empty list
  public ILoGamePiece collidesWithAll(ILoGamePiece other, boolean collided) {
    return this;
  }

  // returns false if the given GamePiece collides with none
  // of the GamePieces in this list
  public boolean collided(IGamePiece other) {
    return false;
  }

  // returns the other list as the ending of the first list
  public ILoGamePiece append(ILoGamePiece other) {
    return other;
  }

  // returns the finished list of split bullets upon reaching
  // the empty list
  public ILoGamePiece explodeAll() {
    return this;
  }
}

// represents a non-empty list of GamePieces
class ConsLoGamePiece implements ILoGamePiece {
  IGamePiece first;
  ILoGamePiece rest;

  ConsLoGamePiece(IGamePiece first, ILoGamePiece rest) {
    this.first = first;
    this.rest = rest;
  }

  // move every ship in a list
  public ILoGamePiece moveAll() {
    return new ConsLoGamePiece(this.first.move(), this.rest.moveAll());
  }

  // removes every ship in the list that is off screen
  public ILoGamePiece removeOffScreen(int width, int height) {
    if (this.first.isOffScreenGamePiece(width, height)) {
      return this.rest.removeOffScreen(width, height);
    }
    else {
      return new ConsLoGamePiece(this.first, this.rest.removeOffScreen(width, height));
    }
  }

  // places all the GamePieces in a list on a scene
  public WorldScene placeAll(WorldScene scene) {
    return this.rest.placeAll(this.first.place(scene));
  }

  // finds the length of this list by counting every element in
  // the list except the empty list
  public int length(int l) {
    return this.rest.length(l + 1);
  }

  // returns true if the given GamePiece collides with any element
  // from this list
  public boolean collided(IGamePiece other) {
    return this.first.collidesWith(other) || this.rest.collided(other);
  }

  // if collided is set to false, returns this list of GamePieces that haven't
  // hit/been hit
  // by any element in the given list of GamePieces and if collided is set to
  // true,
  // returns the list of GamePieces that have hit/been hit by any element in the
  // given
  // list of GamePieces
  public ILoGamePiece collidesWithAll(ILoGamePiece other, boolean collided) {
    if (other.collided(this.first) == collided) {
      return new ConsLoGamePiece(this.first, this.rest.collidesWithAll(other, collided));
    }
    else {
      return this.rest.collidesWithAll(other, collided);
    }
  }

  // appends two lists of GamePieces
  public ILoGamePiece append(ILoGamePiece other) {
    return new ConsLoGamePiece(this.first, this.rest.append(other));
  }

  // returns the final list of split bullets from this list of destroyed
  // bullets
  public ILoGamePiece explodeAll() {
    return this.first.explosion().append(this.rest.explodeAll());
  }
}

// utility class for functions
class Utils {
  // returns between 1-3 new ships every second with random heights and sides they
  // spawn on
  ILoGamePiece spawnShips(ILoGamePiece oldList) {
    int numOfShips = new Random().nextInt(3);
    if (numOfShips == 0) {
      return new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)), oldList);
    }
    else if (numOfShips == 1) {
      return new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)),
          new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)), oldList));
    }
    else {
      return new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)),
          new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)),
              new ConsLoGamePiece(shipRandomizer(new Random().nextInt(2)), oldList)));
    }
  }

  // returns a ship that spawns on either the left or right side of
  // the screen at a random height
  IGamePiece shipRandomizer(int r) {
    if (r == 0) {
      return new Ship(new MyPosn(0, (new Random().nextInt(200) + 50)), new MyPosn(4, 0),
          IGamePiece.shipRadius);
    }
    else {
      return new Ship(new MyPosn(500, (new Random().nextInt(200) + 50)), new MyPosn(-4, 0),
          IGamePiece.shipRadius);
    }
  }
}

// represents the World class for our game
class MyGame extends World {
  int width;
  int height;
  int currentTick;
  IGamePiece ship;
  IGamePiece bullet;
  ILoGamePiece listOfShips;
  ILoGamePiece listOfBullets;
  int bulletCount;
  int score;

  MyGame(int width, int height, int currentTick, IGamePiece ship, IGamePiece bullet,
      ILoGamePiece listOfShips, ILoGamePiece listOfBullets, int bulletCount, int score) {
    this.width = IGamePiece.width;
    this.height = IGamePiece.height;
    this.currentTick = currentTick;
    this.ship = ship;
    this.bullet = bullet;
    this.listOfShips = listOfShips;
    this.listOfBullets = listOfBullets;
    this.bulletCount = bulletCount;
    this.score = score;
  }

  MyGame(int bulletCount) {
    this.width = IGamePiece.width;
    this.height = IGamePiece.height;
    this.currentTick = 1;
    this.ship = new Ship(new MyPosn(250, 225), IGamePiece.shipVelocity, IGamePiece.shipRadius);
    this.bullet = new Bullet(new MyPosn(250, 300), new MyPosn(0, -8), 2, 1);
    this.listOfShips = new MtLoGamePiece();
    this.listOfBullets = new MtLoGamePiece();
    this.bulletCount = bulletCount;
    this.score = 0;
  }

  // create the WorldScene for the game
  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);

    scene = this.addInfo(scene);

    scene = this.addInfo2(scene);

    scene = this.addShipsToScene(scene);

    scene = this.addBulletsToScene(scene);

    return scene;
  }

  // adds the list of ships to the scene
  WorldScene addShipsToScene(WorldScene scene) {
    return this.listOfShips.placeAll(scene);
  }

  // adds the list of bullets to the scene
  WorldScene addBulletsToScene(WorldScene scene) {
    return this.listOfBullets.placeAll(scene);
  }

  // adds the bullet count to the screen
  WorldScene addInfo(WorldScene scene) {
    return scene.placeImageXY(
        new TextImage("Bullet Count: " + Integer.toString(this.bulletCount), Color.black), 50, 10);
  }

  // adds the score to the screen
  WorldScene addInfo2(WorldScene scene) {
    return scene.placeImageXY(
        new TextImage("Ships Destroyed: " + Integer.toString(this.score), Color.black), 430, 10);
  }

  // returns a set of methods every tick of the program
  @Override
  public MyGame onTick() {
    return this.moveShips().moveBullets().removeOffScreenGamePiece().removeCollidedGamePiece()
        .incrementTick().spawnRandomShips();
  }

  // moves every ship in the list of ships by its velocity
  public MyGame moveShips() {
    return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
        this.listOfShips.moveAll(), this.listOfBullets, this.bulletCount, this.score);
  }

  // moves every bullet in the list of bullets by its velocity
  public MyGame moveBullets() {
    return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
        this.listOfShips, this.listOfBullets.moveAll(), this.bulletCount, this.score);
  }

  // removes every GamePiece that is off screen
  public MyGame removeOffScreenGamePiece() {
    return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
        this.listOfShips.removeOffScreen(this.width, this.height),
        this.listOfBullets.removeOffScreen(this.width, this.height), this.bulletCount, this.score);
  }

  // removes every GamePiece that has collided with another GamePiece
  public MyGame removeCollidedGamePiece() {
    ILoGamePiece intactShips = this.listOfShips.collidesWithAll(this.listOfBullets, false);
    ILoGamePiece destroyedShips = this.listOfShips.collidesWithAll(this.listOfBullets, true);
    ILoGamePiece intactBullets = this.listOfBullets.collidesWithAll(this.listOfShips, false);
    ILoGamePiece destroyedBullets = this.listOfBullets.collidesWithAll(this.listOfShips, true);

    return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
        intactShips, intactBullets.append(destroyedBullets.explodeAll()), this.bulletCount,
        this.score + destroyedShips.length(0));
  }

  // increments the currentTick field
  public MyGame incrementTick() {
    return new MyGame(this.width, this.height, this.currentTick + 1, this.ship, this.bullet,
        this.listOfShips, this.listOfBullets, this.bulletCount, this.score);
  }

  // spawns a random number of ships between 1-3 on random sides
  // and at random heights every second
  public MyGame spawnRandomShips() {
    if (this.currentTick % 28 == 0) {
      return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
          new Utils().spawnShips(this.listOfShips), this.listOfBullets, this.bulletCount,
          this.score);
    }
    else {
      return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
          this.listOfShips, this.listOfBullets, this.bulletCount, this.score);
    }
  }

  // spawns a bullet at the bottom of the screen whenever
  // the spacebar is pressed
  public MyGame onKeyEvent(String key) {
    if (key.equals(" ") && this.bulletCount > 0) {
      return new MyGame(this.width, this.height, this.currentTick, this.ship, this.bullet,
          this.listOfShips, new ConsLoGamePiece(this.bullet, this.listOfBullets),
          this.bulletCount - 1, this.score);
    }
    else {
      return this;
    }
  }

  // ends the world when the bulletCount reaches 0
  @Override
  public WorldEnd worldEnds() {
    if (this.bulletCount == 0 && this.listOfBullets.length(0) == 0) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // returns the endScene WorldScene
  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(this.width, this.height);
    return endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 150);
  }
}

/*
 * class ExamplesMyGame { IGamePiece testShip = new Ship(new MyPosn(250, 225),
 * IGamePiece.shipVelocity, IGamePiece.shipRadius);
 * 
 * IGamePiece testBullet = new Bullet(new MyPosn(250, 300), new MyPosn(0, -8),
 * 2, 1);
 * 
 * ILoGamePiece emptyShip = new MtLoGamePiece(); ILoGamePiece emptyBullet = new
 * MtLoGamePiece();
 * 
 * boolean testBigBang(Tester t) { MyGame world = new MyGame(IGamePiece.width,
 * IGamePiece.height, 1, this.testShip, this.testBullet, this.emptyShip,
 * this.emptyBullet, 10, 0); return world.bigBang(500, 300, 1.0/28.0); } }
 */
class ExamplesMyGame {

  // examples
  MyPosn bulletVelocity = new MyPosn(0, -8);
  int bullet_radius = 2;
  int n = 1;

  IGamePiece testShip1 = new Ship(new MyPosn(250, 225), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip2 = new Ship(new MyPosn(250, 150), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip3 = new Ship(new MyPosn(250, 75), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);

  IGamePiece testBullet = new Bullet(new MyPosn(250, 300), bulletVelocity, bullet_radius, 1);

  ILoGamePiece los1 = new ConsLoGamePiece(this.testShip1, new ConsLoGamePiece(this.testShip2,
      new ConsLoGamePiece(this.testShip3, new MtLoGamePiece())));
  ILoGamePiece emptyShip = new MtLoGamePiece();
  ILoGamePiece emptyBullet = new MtLoGamePiece();

  WorldScene scene1 = new WorldScene(500, 500);

  boolean testBigBang(Tester t) {
    MyGame world = new MyGame(IGamePiece.width, IGamePiece.height, 1, this.testShip1,
        this.testBullet, this.emptyShip, this.emptyBullet, 10, 0);
    return world.bigBang(500, 300, 1.0 / 28.0);
  }

  // examples for testing ifOffScreen
  MyPosn posn1 = new MyPosn(-70, 75);
  MyPosn posn2 = new MyPosn(65, 700);
  MyPosn posn3 = new MyPosn(250, -30);
  MyPosn posn4 = new MyPosn(700, 300);
  MyPosn posn5 = new MyPosn(300, 300);

  // tester for method isOffScreen
  boolean testIsOffScreen(Tester t) {
    return t.checkExpect(posn1.isOffScreen(500, 500), true)
        && t.checkExpect(posn2.isOffScreen(500, 500), true)
        && t.checkExpect(posn3.isOffScreen(500, 500), true)
        && t.checkExpect(posn4.isOffScreen(500, 500), true)
        && t.checkExpect(posn5.isOffScreen(500, 500), false);

  }

  // examples for testing isOffScreenGamePiece
  IGamePiece testShip4 = new Ship(posn1, IGamePiece.shipVelocity, IGamePiece.shipRadius);
  IGamePiece testShip5 = new Ship(posn2, IGamePiece.shipVelocity, IGamePiece.shipRadius);
  IGamePiece testShip6 = new Ship(posn3, IGamePiece.shipVelocity, IGamePiece.shipRadius);
  IGamePiece testShip7 = new Ship(posn5, IGamePiece.shipVelocity, IGamePiece.shipRadius);
  IGamePiece testBullet1 = new Bullet(posn4, bulletVelocity, bullet_radius, n);

  // tester for method isOffScreenGamePiece
  boolean testIsOffScreenGamePiece(Tester t) {
    return t.checkExpect(testShip4.isOffScreenGamePiece(500, 500), true)
        && t.checkExpect(testShip5.isOffScreenGamePiece(500, 500), true)
        && t.checkExpect(testShip6.isOffScreenGamePiece(500, 500), true)
        && t.checkExpect(testShip7.isOffScreenGamePiece(500, 500), false)
        && t.checkExpect(testBullet1.isOffScreenGamePiece(500, 500), true);
  }

  // examples for testing collidesWith
  IGamePiece testShip8 = new Ship(new MyPosn(250, 225), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip9 = new Ship(new MyPosn(250, 200), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip10 = new Ship(new MyPosn(250, 215), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip11 = new Ship(new MyPosn(250, 223), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip12 = new Ship(new MyPosn(200, 250), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip13 = new Ship(new MyPosn(210, 250), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip14 = new Ship(new MyPosn(211, 250), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testShip15 = new Ship(new MyPosn(250, 225), IGamePiece.shipVelocity,
      IGamePiece.shipRadius);
  IGamePiece testBullet2 = new Bullet(new MyPosn(250, 225), bulletVelocity, bullet_radius, n);

  // tester for method collidesWith
  boolean testCollidesWith(Tester t) {
    return t.checkExpect(testShip8.collidesWith(testShip9), false)
        && t.checkExpect(testShip8.collidesWith(testShip10), true)
        && t.checkExpect(testShip8.collidesWith(testShip11), true)
        && t.checkExpect(testShip12.collidesWith(testShip13), true)
        && t.checkExpect(testShip12.collidesWith(testShip14), true)
        && t.checkExpect(testBullet2.collidesWith(testShip15), true)
        && t.checkExpect(testBullet2.collidesWith(testShip14), false);
  }

  // tester for method collidesWithAll
  boolean testCollidesWithAll(Tester t) {
    return t.checkExpect(mtLoGamePieces.collidesWithAll(loShip1, false), mtLoGamePieces)
        && t.checkExpect(loShip1.collidesWithAll(loShip2, true),
            new ConsLoGamePiece(new Ship(new MyPosn(250, 150), new MyPosn(4, 0), 10),
                new ConsLoGamePiece(new Ship(new MyPosn(250, 75), new MyPosn(4, 0), 10),
                    new MtLoGamePiece())));
  }

  // tester for method collided
  boolean testCollided(Tester t) {
    return t.checkExpect(mtLoGamePieces.collided(testShip9), false)
        && t.checkExpect(loShip1.collided(testShip10), true);
  }
  /*
   * No tests because it doesn't fully work. Sorry about that.
   * 
   * // tester for method explosion boolean testExplosion(Tester t) { return
   * t.checkExpect(testShip1.explosion(), new MtLoGamePiece()) &&
   * t.checkExpect(testBullet.explosion(), testBullet.explosionHelper(n)); //
   * change to not use helper }
   * 
   * boolean testExplosionHelper(Tester t) { return
   * t.checkExpect(testBullet1.explosionHelper(1), new MtLoGamePiece()) &&
   * t.checkExpect(testBullet.explosionHelper(2), testBullet.explosionHelper(n));
   * }
   */

  // tester for method getLengthX
  boolean testGetLengthX(Tester t) {
    return t.checkInexact(testShip1.getLengthX(250), 0.0, 0.001)
        && t.checkInexact(testShip2.getLengthX(260), 10.0, 0.001)
        && t.checkInexact(testShip3.getLengthX(240), 10.0, 0.001);
  }

  // tester for method getLengthY
  boolean testGetLengthY(Tester t) {
    return t.checkInexact(testShip1.getLengthY(225), 0.0, 0.001)
        && t.checkInexact(testShip2.getLengthY(255), 105.0, 0.001)
        && t.checkInexact(testShip3.getLengthY(55), 20.0, 0.001);
  }

  // tester for method move
  boolean testMove(Tester t) {
    return t.checkExpect(testShip1.move(),
        new Ship(new MyPosn(250 + 4, 225), IGamePiece.shipVelocity, IGamePiece.shipRadius))
        && t.checkExpect(testShip2.move(),
            new Ship(new MyPosn(250 + 4, 150), IGamePiece.shipVelocity, IGamePiece.shipRadius))
        && t.checkExpect(testBullet.move(),
            new Bullet(new MyPosn(250, 225 - 8), bulletVelocity, bullet_radius, n))
        && t.checkExpect(testBullet1.move(),
            new Bullet(new MyPosn(700, 300 - 8), bulletVelocity, bullet_radius, n));
  }

  // examples for moveAll
  ILoGamePiece mtLoGamePieces = new MtLoGamePiece();
  ILoGamePiece loShip1 = new ConsLoGamePiece(testShip1,
      new ConsLoGamePiece(testShip2, new ConsLoGamePiece(testShip3, mtLoGamePieces)));
  ILoGamePiece loShip1TestResult = new ConsLoGamePiece(
      new Ship(new MyPosn(250 + 4, 225), IGamePiece.shipVelocity, IGamePiece.shipRadius),
      new ConsLoGamePiece(
          new Ship(new MyPosn(250 + 4, 150), IGamePiece.shipVelocity, IGamePiece.shipRadius),
          new ConsLoGamePiece(
              new Ship(new MyPosn(250 + 4, 75), IGamePiece.shipVelocity, IGamePiece.shipRadius),
              mtLoGamePieces)));
  ILoGamePiece loBullet1 = new ConsLoGamePiece(testBullet,
      new ConsLoGamePiece(testBullet1, mtLoGamePieces));
  ILoGamePiece loBullet1TestResult = new ConsLoGamePiece(
      new Bullet(new MyPosn(250, 225 - 8), bulletVelocity, bullet_radius, n), new ConsLoGamePiece(
          new Bullet(new MyPosn(700, 300 - 8), bulletVelocity, bullet_radius, n), mtLoGamePieces));

  // tester for method moveAll
  boolean testMoveAll(Tester t) {
    return t.checkExpect(mtLoGamePieces.moveAll(), new MtLoGamePiece())
        && t.checkExpect(loShip1.moveAll(), loShip1TestResult)
        && t.checkExpect(loBullet1.moveAll(), loBullet1TestResult);
  }

  // examples for removeOffScreen
  ILoGamePiece loBullet2 = new ConsLoGamePiece(testBullet,
      new ConsLoGamePiece(testBullet1, new ConsLoGamePiece(testBullet2, mtLoGamePieces)));
  ILoGamePiece loBullet2TestResult = new ConsLoGamePiece(
      new Bullet(new MyPosn(250, 225 - 8), bulletVelocity, bullet_radius, n), new ConsLoGamePiece(
          new Bullet(new MyPosn(250, 225), bulletVelocity, bullet_radius, n), mtLoGamePieces));
  ILoGamePiece loShip2 = new ConsLoGamePiece(testShip4,
      new ConsLoGamePiece(testShip2, new ConsLoGamePiece(testShip3, mtLoGamePieces)));
  ILoGamePiece loShip2TestResult = new ConsLoGamePiece(
      new Ship(new MyPosn(250 + 4, 150), IGamePiece.shipVelocity, IGamePiece.shipRadius),
      new ConsLoGamePiece(
          new Ship(new MyPosn(250 + 4, 75), IGamePiece.shipVelocity, IGamePiece.shipRadius),
          mtLoGamePieces));

  // tester for method removeOffScreen
  boolean testRemoveOffScreen(Tester t) {
    return t.checkExpect(mtLoGamePieces.removeOffScreen(500, 500), new MtLoGamePiece())
        && t.checkExpect(loShip2.removeOffScreen(500, 500), loShip2TestResult)
        && t.checkExpect(loBullet2.removeOffScreen(500, 500), loBullet2TestResult);
  }

  // tester for method length
  boolean testLength(Tester t) {
    return t.checkExpect(mtLoGamePieces.length(0), 0) && t.checkExpect(loShip2.length(0), 3)
        && t.checkExpect(loBullet1.length(0), 2);
  }

  // examples for moveShips
  MyGame game1 = new MyGame(500, 500, 0, testShip1, testBullet, loShip1, loBullet1, 0, 0);
  MyGame gameTestResult1 = new MyGame(500, 500, 0, testShip1, testBullet, loShip1TestResult,
      loBullet1, 0, 0);

  MyGame mtShipGame = new MyGame(500, 500, 0, testShip1, testBullet, mtLoGamePieces, loBullet1, 2,
      0);
  MyGame mtShipGameTestResult = new MyGame(500, 500, 0, testShip1, testBullet, mtLoGamePieces,
      loBullet1, 2, 0);

  // tester for method moveShips
  boolean testMoveShips(Tester t) {
    return t.checkExpect(mtShipGame.moveShips(), mtShipGameTestResult)
        && t.checkExpect(game1.moveShips(), gameTestResult1);
  }

  // examples for moveBullets
  MyGame game2 = new MyGame(500, 500, 0, testShip1, testBullet, loShip1, loBullet1, 3, 0);
  MyGame gameTestResult2 = new MyGame(500, 500, 0, testShip1, testBullet, loShip1,
      loBullet1TestResult, 3, 0);

  MyGame mtBulletGame = new MyGame(500, 500, 0, testShip1, testBullet, loShip1, mtLoGamePieces, 0,
      0);
  MyGame mtBulletGameTestResult = new MyGame(500, 500, 0, testShip1, testBullet, loShip1,
      mtLoGamePieces, 0, 0);

  // tester for method moveBullets
  boolean testMoveBullets(Tester t) {
    return t.checkExpect(mtBulletGame.moveBullets(), mtBulletGameTestResult)
        && t.checkExpect(game2.moveBullets(), gameTestResult2);
  }

  // examples for addShipsToScene
  MyGame mtGameScene = new MyGame(500, 500, 0, testShip1, testBullet, mtLoGamePieces,
      mtLoGamePieces, 0, 0);

  // tester for method addShipsToScene
  boolean testAddShipsToScene(Tester t) {
    return t.checkExpect(mtGameScene.addShipsToScene(scene1), scene1)
        && t.checkExpect(game1.addShipsToScene(scene1), game1.listOfShips.placeAll(scene1));
  }

  // tester for method addBulletsToScene
  boolean testAddBulletsToScene(Tester t) {
    return t.checkExpect(mtGameScene.addBulletsToScene(scene1), scene1)
        && t.checkExpect(mtShipGameTestResult.addBulletsToScene(scene1),
            mtShipGameTestResult.listOfBullets.placeAll(scene1));
  }

  // tester for method addInfo
  boolean testAddInfo(Tester t) {
    return t.checkExpect(mtGameScene.addInfo(scene1),
        scene1.placeImageXY(new TextImage("Bullet Count: " + Integer.toString(0), Color.black), 50,
            10))
        && t.checkExpect(game2.addInfo(scene1), scene1.placeImageXY(
            new TextImage("Bullet Count: " + Integer.toString(3), Color.black), 50, 10));
  }

  // tester for method incrementTick
  boolean testIncrementTick(Tester t) {
    return t.checkExpect(mtGameScene.incrementTick(),
        new MyGame(500, 500, 1, testShip1, testBullet, mtLoGamePieces, mtLoGamePieces, 0, 0))
        && t.checkExpect(game1.incrementTick(),
            new MyGame(500, 500, 1, testShip1, testBullet, loShip1, loBullet1, 0, 0));
  }

  // tester for method onKeyEvent
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(mtGameScene.onKeyEvent("a"), mtGameScene)
        && t.checkExpect(mtGameScene.onKeyEvent(" "), mtGameScene)
        && t.checkExpect(game1.onKeyEvent("a"), game1);
  }

  // tester for method worldEnds
  boolean testWorldEnds(Tester t) {
    return t.checkExpect(mtGameScene.worldEnds(), new WorldEnd(true, mtGameScene.makeEndScene()))
        && t.checkExpect(game1.worldEnds(), new WorldEnd(false, game1.makeEndScene()));
  }

  // tester for method makeEndScene
  boolean testMakeEndScene(Tester t) {
    WorldScene endScene = new WorldScene(500, 300);

    return t.checkExpect(mtGameScene.makeEndScene(),
        endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 150))
        && t.checkExpect(game1.makeEndScene(),
            endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 150));
  }

  WorldImage drawnBullet1 = new CircleImage(2, OutlineMode.SOLID, Color.pink);
  WorldImage drawnBullet2 = new CircleImage(2, OutlineMode.SOLID, Color.pink);

  // tester for method drawBullet
  boolean testDrawBullet(Tester t) {
    return t.checkExpect(
        (new Bullet(new MyPosn(250, 300), bulletVelocity, bullet_radius, n)).drawBullet(),
        drawnBullet1)
        && t.checkExpect((new Bullet(posn4, bulletVelocity, bullet_radius, n)).drawBullet(),
            drawnBullet2);
  }

  WorldImage drawnShip1 = new CircleImage(10, OutlineMode.SOLID, Color.cyan);
  WorldImage drawnShip2 = new CircleImage(10, OutlineMode.SOLID, Color.cyan);

  // tester for method drawShip
  boolean testDrawShip(Tester t) {
    return t.checkExpect(
        (new Ship(new MyPosn(250, 225), IGamePiece.shipVelocity, IGamePiece.shipRadius))
            .drawShip(),
        drawnShip1)
        && t.checkExpect(
            (new Ship(new MyPosn(250, 75), IGamePiece.shipVelocity, IGamePiece.shipRadius))
                .drawShip(),
            drawnShip2);
  }

  // tester for method place
  boolean testPlace(Tester t) {
    return t.checkExpect(testShip1.place(scene1), scene1.placeImageXY(drawnShip1, 250, 225))
        && t.checkExpect(testBullet.place(scene1), scene1.placeImageXY(drawnBullet1, 250, 300));
  }

  int numOfShips1 = new Random(1).nextInt(3);
  int numOfShips2 = new Random(0).nextInt(3);
  int numOfShips3 = new Random(4).nextInt(3);

  // tester for method shipRandomizer
  boolean testShipRandomizer(Tester t) {
    return t.checkExpect(new Utils().shipRandomizer(numOfShips1),
        new Ship(new MyPosn(0, (numOfShips1 + 50)), new MyPosn(4, 0), 10))
        && t.checkExpect(new Utils().shipRandomizer(numOfShips2),
            new Ship(new MyPosn(0, (numOfShips2 + 50)), new MyPosn(4, 0), 10))
        && t.checkExpect(new Utils().shipRandomizer(numOfShips3),
            new Ship(new MyPosn(0, (numOfShips3 + 50)), new MyPosn(4, 0), 10));
  }

  int numOfShips4 = new Random(1).nextInt(200);
  int numOfShips5 = new Random(0).nextInt(200);
  int numOfShips6 = new Random(4).nextInt(200);

  // tester for method spawnShips
  boolean testSpawnShips(Tester t) {
    return t.checkExpect(new Utils().spawnShips(mtLoGamePieces),
        new ConsLoGamePiece(new Ship(new MyPosn(0, (numOfShips5 + 50)), new MyPosn(4, 0), 10),
            mtLoGamePieces))
        && t.checkExpect(new Utils().spawnShips(loShip1),
            new ConsLoGamePiece(new Ship(new MyPosn(0, (numOfShips4 + 50)), new MyPosn(4, 0), 10),
                new ConsLoGamePiece(
                    new Ship(new MyPosn(0, (numOfShips4 + 50)), new MyPosn(4, 0), 10), loShip1)))
        && t.checkExpect(new Utils().spawnShips(loShip2),
            new ConsLoGamePiece(new Ship(new MyPosn(0, (numOfShips6 + 50)), new MyPosn(4, 0), 10),
                new ConsLoGamePiece(
                    new Ship(new MyPosn(0, (numOfShips6 + 50)), new MyPosn(4, 0), 10),
                    new ConsLoGamePiece(
                        new Ship(new MyPosn(0, (numOfShips6 + 50)), new MyPosn(4, 0), 10),
                        loShip2))));
  }

  // tester for method spawnRandomShips
  boolean testSpawnRandomShips(Tester t) {
    return t.checkExpect(mtGameScene.spawnRandomShips(),
        new MyGame(500, 500, 0, testShip1, testBullet, new Utils().spawnShips(loShip1),
            mtLoGamePieces, 0, 0))
        && t.checkExpect(game1.spawnRandomShips(), new MyGame(500, 500, 0, testShip1, testBullet,
            new Utils().spawnShips(loShip1), loBullet1, 2, 0));
  }

  // should not use collide methods
  // tester for method removeCollidedGamePiece
  boolean testremoveCollidedGamePiece(Tester t) {
    return t.checkExpect(mtGameScene.removeCollidedGamePiece(),
        new MyGame(500, 500, 0, testShip1, testBullet, mtLoGamePieces, mtLoGamePieces, 0, 0))
        && t.checkExpect(game1.removeCollidedGamePiece(),
            new MyGame(500, 500, 0, testShip1, testBullet, loShip1, loBullet1, 0, 0));
  }

}