import tester.*;

// to represent a piece of embroidery with a name and motif
class EmbroideryPiece {
  /*
   * TEMPLATE
   * Fields:
   * this.name ... String
   * this.motif ... ILoMotif
   * Methods:
   * this.embroideryInfo() ... String
   * this.averageDifficulty() ... double
   * Methods on Fields:
   * this.motif.count() ... int
   * this.motif.averageDifficulty() ... double
   * this.embroideryInfo() ... String
   */
  String name;
  IMotif motif;

  // constructor for an EmbroideryPiece
  EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }
  
  public int count() {
    return 0;
  }

  // adding the name of the embroidery to the finished embroideryInfo
  public String embroideryInfo() {
    /*
     * TEMPLATE
     * Fields on Parameters:
     * this.name ... String
     * this.motif ... ILoMotif
     * this.first ... ILoMotif
     * this.motifs ... ILoMotif
     * this.description ... String
     * Methods on Parameters:
     * this.motif.embroideryInfo() ... String
     * this.motifs.embroideryInfo() ... String
     * this.first.embroideryInfo() ... String
     */

    return this.name + ": " + this.motif.embroideryInfo() + ".";
  }

  // dividing the sum of the difficulties by the count of the motifs to get the averageDifficulty
  // for the entire embroidery piece
  public double averageDifficulty() {
    if (this.motif.count() > 0) {
      return this.motif.averageDifficulty() / this.motif.count();
    }
    else {
      return 0.0;
    }
  }
}

interface IMotif {
  
  // to count the number of motifs
  int count();
  
  // to calculate the average difficulty of all the stitches in an embroidery
  // piece
  double averageDifficulty();
  
  // to track various info about the motifs in an embroidery piece
  String embroideryInfo();
}

// to represent a list of motifs for embroidery
interface ILoMotif {

  // to count the number of motifs
  int count();
  /*
   * TEMPLATE
   * Fields on Parameter:
   * this.motifs ... ILoMotif
   * this.motif ... ILoMotif
   * Methods on Parameter:
   * this.motifs.count() ... int
   * this.motif.count() ... int
   */

  // to calculate the average difficulty of all the stitches in an embroidery
  // piece
  double averageDifficulty();
  /*
   * TEMPLATE
   * Fields on Parameter:
   * this.motifs ... ILoMotif
   * this.motif ... ILoMotif
   * this.first ... ILoMotif
   * this.difficulty ... double
   * Methods on Parameter:
   * this.motifs.averageDifficulty() ... double
   * this.motif.averageDifficulty() ... double
   * this.first.averageDifficulty() ... double
   */

  // to track various info about the various motifs
  String embroideryInfo();
}

// to represent empty lists of motifs
class MtLoMotif implements ILoMotif {
  MtLoMotif() {
  }
  /*TEMPLATE
   * Fields:
   * N/A
   * Methods:
   * this.averageDifficulty() ... double
   * this.count() ... int
   * this.embroideryInfo() ... String
   * Methods for Fields:
   * N/A
   */

  // empty list so difficulty is 0
  public double averageDifficulty() {
    return 0;
  }

  // empty list so count is 0
  public int count() {
    return 0;
  }

  // empty list so shouldn't add anything to embroideryInfo
  public String embroideryInfo() {
    return "";
  }
}

// to represent non-empty lists of motifs
class ConsLoMotif implements ILoMotif {
  /* TEMPLATE
   * Fields:
   * this.first ... IMotif
   * this.rest ... ILoMotif
   * Methods:
   * this.count() ... int
   * this.averageDifficulty() ... double
   * this.embroideryInfo() ... String
   * Methods of Fields:
   * this.first.count() ... int
   * this.rest.count() ... int
   * this.first.averageDifficulty() ... double
   * this.rest.averageDifficulty() ... double
   * this.first.embroideryInfo() ... String
   * this.rest.embroideryInfo() ... String
   */
  
  IMotif first;
  ILoMotif rest;

  // constructor for a ConsLoMotif
  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  // counting the number of motifs in a list of motifs
  public int count() {
    return this.first.count() + this.rest.count();
  }

  // finding the sum of the difficulties of the different motifs to find the
  // average
  public double averageDifficulty() {
    return this.first.averageDifficulty() + this.rest.averageDifficulty();
  }

  // add the embroideryInfo of the first motif in the list to the rest of the
  // motifs
  public String embroideryInfo() {
    if (this.rest.embroideryInfo().equals("")) {
      return this.first.embroideryInfo();
    }
    else {
      return this.first.embroideryInfo() + ", " + this.rest.embroideryInfo();
    }
  }
}

// to represent a group of motifs
class GroupMotif implements IMotif {
  /*TEMPLATE
   * Fields:
   * this.description ... String
   * this.motifs ... ILoMotif
   * Methods:
   * this.count() ... int
   * this.averageDifficulty() ... double
   * this.embroideryInfo() ... String
   * Methods of Fields:
   * this.motifs.count() ... int
   * this.motifs.averageDifficulty() ... double
   * this.motifs.embroideryInfo() ... String
   */
  
  String description;
  ILoMotif motifs;

  // constructor for a group of motifs
  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  // total count of motifs
  public int count() {
    return this.motifs.count();
  }

  // average difficulty of all motifs in a piece of embroidery
  public double averageDifficulty() {
    return this.motifs.averageDifficulty();
  }

  // return the embroideryInfo from the ConsLoMotif list
  public String embroideryInfo() {
    return this.motifs.embroideryInfo();
  }
}

// to represent a motif made using cross stitching
class CrossStitchMotif implements IMotif {
  /*TEMPLATE
   * Fields:
   * this.description ... String
   * this.difficulty ... double
   * Methods:
   * this.count() ... int
   * this.averageDifficulty ... double
   * this.embroideryInfo ... String
   * Methods of Fields:
   * N/A
   */
  
  String description;
  double difficulty;

  // constructor for a CrossStitchMotif
  CrossStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  // add the cross stitch motif to the count
  public int count() {
    return 1;
  }

  // add the difficulty of the motif to the sum
  public double averageDifficulty() {
    return this.difficulty;
  }

  // create the embroidery info for a cross stitch motif
  public String embroideryInfo() {
    return this.description + " (cross stitch)";
  }
}

// to represent a motif made using chain stitching
class ChainStitchMotif implements IMotif {
  /* TEMPLATE
   * Fields:
   * this.description ... String
   * this.difficulty ... double
   * Methods:
   * this.count() ... int
   * this.averageDifficulty() ... double
   * this.embroideryInfo() ... String
   * Methods of Fields:
   * N/A
   */
  
  String description;
  double difficulty;

  // constructor for a ChainStitchMotif
  ChainStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  // add the chain stitch motif to the count
  public int count() {
    return 1;
  }

  // add the difficulty to the sum
  public double averageDifficulty() {
    return this.difficulty;
  }

  // create the embroideryInfo for a chain stitch motif
  public String embroideryInfo() {
    return this.description + " (chain stitch)";
  }
}

// to represent examples of embroidery
class ExamplesEmbroidery {
  // examples of different motifs
  ILoMotif empty = new MtLoMotif();
  IMotif bird = new CrossStitchMotif("bird", 4.5);
  IMotif tree = new ChainStitchMotif("tree", 3.0);
  IMotif rose = new CrossStitchMotif("rose", 5.0);
  IMotif poppy = new ChainStitchMotif("poppy", 4.75);
  IMotif daisy = new CrossStitchMotif("daisy", 3.2);

  // examples of lists of motifs
  ILoMotif flowersList = new ConsLoMotif(this.rose,
      new ConsLoMotif(this.poppy, new ConsLoMotif(this.daisy, this.empty)));
  IMotif flowers = new GroupMotif("flowers", this.flowersList);
  ILoMotif natureList = new ConsLoMotif(this.bird,
      new ConsLoMotif(this.tree, new ConsLoMotif(this.flowers, this.empty)));
  IMotif nature = new GroupMotif("nature", this.natureList);
  IMotif emptyGroup = new GroupMotif("empty piece", new MtLoMotif());
  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", this.nature);

  // test to ensure count method for averageDifficulty works
  boolean testCount(Tester t) {
    return t.checkExpect(this.nature.count(), 5);
  }

  // test to ensure averageDifficulty method works
  boolean testAverageDifficulty(Tester t) {
    return t.checkExpect(this.pillowCover.averageDifficulty(), 4.09);
  }

  // test to ensure embroideryInfo method works
  boolean testEmbroideryInfo(Tester t) {
    return t.checkExpect(this.pillowCover.embroideryInfo(),
        "Pillow Cover: bird (cross stitch),"
        + " tree (chain stitch), rose (cross stitch),"
        + " poppy (chain stitch), daisy (cross stitch).");
  }
  
  boolean testAverageDifficulty2(Tester t) {
    return t.checkExpect(this.emptyGroup.averageDifficulty(), 0.0);
  }
}
