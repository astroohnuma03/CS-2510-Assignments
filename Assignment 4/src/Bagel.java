import tester.Tester;

// to represent the ingredients needed to make a bagel
class BagelRecipe {
  /*
   * TEMPLATE
   * Fields:
   * this.flour ... double
   * this.water ... double
   * this.yeast ... double
   * this.salt ... double
   * this.malt ... double
   * Methods:
   * this.sameRecipe(BagelRecipe) ... boolean
   * Methods on Fields:
   * this.sameRecipe(BagelRecipe) ... boolean
   * other.flour ... double
   * other.water ... double
   * other.yeast ... double
   * other.salt ... double
   * other.malt ... double
   */
  
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;
  
  // main constructor which takes all fields and enforces all constraints
  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    this.water = water;

    if (flour == water) {
      this.flour = flour;
    }
    else {
      throw new IllegalArgumentException("Weight of flour does not equal weight of water");
    }

    this.malt = malt;

    if (yeast == malt) {
      this.yeast = yeast;
    }
    else {
      throw new IllegalArgumentException("Weight of yeast does not equal weight of malt");
    }

    if ((salt + yeast) * 20 == flour) {
      this.salt = salt;
    }
    else {
      throw new IllegalArgumentException(
          "Weight of salt + yeast does not equal 1/20th the weight of flour");
    }
  }
  
  // constructor which requires only flour and yeast and produces a perfect bagel
  // recipe
  BagelRecipe(double flour, double yeast) {
    this.flour = flour;
    this.water = flour;
    this.yeast = yeast;
    this.malt = yeast;

    this.salt = (0.05 * flour) - yeast;
  }

  // constructor which takes the flour, yeast, and salt as volumes and tries
  // to produce a perfect bagel recipe
  BagelRecipe(double flour, double yeast, double salt) {
    this.flour = flour * 4.25;
    this.water = flour * 4.25;
    this.yeast = (yeast / 48) * 5;
    this.salt = (salt / 48) * 10;
    
    if (Math.abs(((this.yeast + this.salt) * 20) - this.flour) < 0.001) {
      this.malt = (yeast / 48) * 5;
    }
    else {
      throw new IllegalArgumentException(
          "Weight of salt + yeast does not equal 1/20th the weight of flour");
    }
  }
  
  // check if the ingredients of one BagelRecipe are equal to another within 0.001
  boolean sameRecipe(BagelRecipe other) {
    return (Math.abs(this.flour - other.flour) < 0.001)
        && (Math.abs(this.water - other.water) < 0.001)
        && (Math.abs(this.salt - other.salt) < 0.001)
        && (Math.abs(this.yeast - other.yeast) < 0.001)
        && (Math.abs(this.malt - other.malt) < 0.001);
  }
}

class ExamplesBagelRecipe {
  ExamplesBagelRecipe() {
  }
  
  // examples of normal BagelRecipes
  BagelRecipe recipe1 = new BagelRecipe(1000, 1000, 30, 20, 30);
  BagelRecipe recipe2 = new BagelRecipe(500, 500, 13, 12, 13);
  
  // example for the second constructor
  BagelRecipe recipe3 = new BagelRecipe(1000, 30);
  
  // example for the third constructor with volumes converted back into weight
  // in ounces to check for sameness with the volume recipe
  BagelRecipe recipe4 = new BagelRecipe(212.5, 212.5, 6.425, 4.200, 6.425);
  
  // example for the third constructor which gives the values of flour, yeast,
  // and salt as volumes
  BagelRecipe recipeVol = new BagelRecipe(50, 61.68, 20.16);

  // test if the volume converts correctly back into weight in ounces
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(recipeVol.sameRecipe(recipe4), true)
        && t.checkExpect(recipe3.sameRecipe(recipe1), true)
        && t.checkExpect(recipe1.sameRecipe(recipe2), false);
  }
}