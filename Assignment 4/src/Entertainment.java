import tester.*;

interface IEntertainment {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  // is this Magazine the same as that one?
  boolean sameMagazine(Magazine that);

  // is this TVseries the same as that one?
  boolean sameTVseries(TVSeries that);

  // is this Podcast the same as that one?
  boolean samePodcast(Podcast that);
}

//abstract class to represent Entertainment
abstract class AEntertainment implements IEntertainment {
  /*
   * TEMPLATE
   * Fields:
   * this.name ... String
   * this.price ... double
   * this.installments ... int
   * Methods:
   * this.totalPrice() ... double
   * this.duration() ... int
   * this.format() ... String
   * this.sameMagazine(Magazine) ... boolean
   * this.sameTVseries(TVseries) ... boolean
   * this.samePodcast(Podcast) ... boolean
   * Methods of Fields:
   * N/A
   */
  
  String name;
  double price; // represents price per issue or per episode
  int installments; // number of issues per year or number of episodes of this series or number of
  // episodes in this Podcast

  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  // computes the price to this AEntertainment
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this AEntertainment
  public int duration() {
    return 50 * this.installments;
  }

  // produce a String that shows the name and price of this AEntertainment
  public String format() {
    return this.name + ", " + this.price + ".";
  }

  // is this AEntertainment the same as that IEntertainment?
  public boolean sameMagazine(Magazine that) {
    return false;
  }

  // is this AEntertainment the same as that TVseries?
  public boolean sameTVseries(TVSeries that) {
    return false;
  }

  // is this AEntertainment the same as that Podcast?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

// to represent a magazine as a form of entertainment
class Magazine extends AEntertainment {
  /*
   * TEMPLATE
   * Fields:
   * this.name ... String
   * this.price ... double
   * this.installments ... int
   * this.genre ... String
   * this.pages ... int
   * Methods:
   * this.duration() ... int
   * this.sameEntertainment(IEntertainment) ... boolean
   * this.sameMagazine(Magazine) ... boolean
   * this.sameTVseries(TVseries) ... boolean
   * this.samePodcast(Podcast) ... boolean
   * Methods of Fields:
   * this.name.equals(String) ... boolean
   * that.sameMagazine(this) ... boolean
   */
  
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  @Override
  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  public int duration() {
    return (5 * this.pages) * this.installments;
  }

  // is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }

  // is this Magazine the same as that Magazine?
  public boolean sameMagazine(Magazine that) {
    return this.name.equals(that.name) && (this.price == that.price)
        && (this.installments == that.installments) && (this.genre == that.genre)
        && (this.pages) == (that.pages);
  }

  // is this Magazine the same as that TVseries?
  public boolean sameTVseries(TVSeries that) {
    return false;
  }

  // is this Magazine the same as that Podcast?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

// to represent a TVseries as a form of entertainment
class TVSeries extends AEntertainment {
  /*
   * TEMPLATE
   * Fields:
   * this.name ... String
   * this.price ... double
   * this.installments ... int
   * this.corporation ... String
   * Methods:
   * this.sameEntertainment(IEntertainment) ... boolean
   * this.sameMagazine(Magazine) ... boolean
   * this.sameTVseries(TVseries) ... boolean
   * this.samePodcast(Podcast) ... boolean
   * Methods of Fields:
   * this.name.equals(String) ... boolean
   * that.sameTVseries(this) ... boolean
   */
  
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }

  // is this TVseries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVseries(this);
  }

  // is this TVseries the same as that Magazine?
  public boolean sameMagazine(Magazine that) {
    return false;
  }

  // is this TVseries the same as that TVseries?
  public boolean sameTVseries(TVSeries that) {
    return this.name.equals(that.name) && (this.price == that.price)
        && (this.installments == that.installments) && (this.corporation == that.corporation);
  }

  // is this TVseries the same as that Podcast?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

// to represent a Podcast as a form of entertainment
class Podcast extends AEntertainment {
  /*
   * TEMPLATE
   * Fields:
   * this.name ... String
   * this.price ... double
   * this.installments ... int
   * Methods:
   * this.sameEntertainment(IEntertainment) ... boolean
   * this.sameMagazine(Magazine) ... boolean
   * this.sameTVseries(TVseries) ... boolean
   * this.samePodcast(Podcast) ... boolean
   * Methods of Fields:
   * this.name.equals(String) ... boolean
   * that.samePodcast(this) ... boolean
   */
  
  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  // is this Podcast the same as this IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }

  // is this Podcast the same as this Magazine?
  public boolean sameMagazine(Magazine that) {
    return false;
  }

  // is this Podcast the same as this TVseries?
  public boolean sameTVseries(TVSeries that) {
    return false;
  }

  // is this Podcast the same as this Podcast?
  public boolean samePodcast(Podcast that) {
    return this.name.equals(that.name) && (this.price == that.price)
        && (this.installments == that.installments);
  }
}

// examples of forms of entertainment
class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);

  IEntertainment vogue = new Magazine("Vogue", 3.50, "Fashion", 50, 12);
  IEntertainment squidGame = new TVSeries("Squid Game", 6.20, 9, "Netflix");
  IEntertainment rudeTailsOfMagic = new Podcast("Rude Tails of Magic", 5.0, 50);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.vogue.totalPrice(), 3.50 * 12, .0001)
        && t.checkInexact(this.squidGame.totalPrice(), 6.20 * 9, .0001)
        && t.checkInexact(this.rudeTailsOfMagic.totalPrice(), 5.0 * 50, .0001);
  }

  // testing duration method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), (5 * 60) * 12)
        && t.checkExpect(this.houseOfCards.duration(), 50 * 13)
        && t.checkExpect(this.serial.duration(), 50 * 8)
        && t.checkExpect(this.vogue.duration(), (5 * 50) * 12)
        && t.checkExpect(this.squidGame.duration(), 50 * 9)
        && t.checkExpect(this.rudeTailsOfMagic.duration(), 50 * 50);
  }

  // testing format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.rollingStone.format(), "Rolling Stone, 2.55.")
        && t.checkExpect(this.houseOfCards.format(), "House of Cards, 5.25.")
        && t.checkExpect(this.serial.format(), "Serial, 0.0.")
        && t.checkExpect(this.vogue.format(), "Vogue, 3.5.")
        && t.checkExpect(this.squidGame.format(), "Squid Game, 6.2.")
        && t.checkExpect(this.rudeTailsOfMagic.format(), "Rude Tails of Magic, 5.0.");
  }

  // testing sameEntertainment method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(rollingStone), true)
        && t.checkExpect(this.rollingStone.sameEntertainment(vogue), false)
        && t.checkExpect(this.rollingStone.sameEntertainment(squidGame), false)
        && t.checkExpect(this.squidGame.sameEntertainment(squidGame), true)
        && t.checkExpect(this.squidGame.sameEntertainment(houseOfCards), false)
        && t.checkExpect(this.squidGame.sameEntertainment(rudeTailsOfMagic), false)
        && t.checkExpect(this.serial.sameEntertainment(serial), true)
        && t.checkExpect(this.serial.sameEntertainment(rudeTailsOfMagic), false)
        && t.checkExpect(this.serial.sameEntertainment(squidGame), false);
  }
}