// to represent a travel mechanic in a game
interface ITravel {
}

// to represent a hut
class Hut implements ITravel {
  int capacity;
  int population;

  // constructor for a hut
  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
}

//to represent an inn
class Inn implements ITravel {
  String name;
  int capacity;
  int population;
  int stalls;

  // constructor for an inn
  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
}

// to represent a castle
class Castle implements ITravel {
  String name;
  String familyName;
  int population;
  int carriageHouse;

  // constructor for a castle
  Castle(String name, String familyName, int population, int carriageHouse) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriageHouse = carriageHouse;
  }
}

// to represent a horse
class Horse implements ITravel {
  ITravel from;
  ITravel to;
  String name;
  String color;

  // constructor for a horse
  Horse(ITravel from, ITravel to, String name, String color) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.color = color;
  }
}

// to represent a carriage
class Carriage implements ITravel {
  ITravel from;
  ITravel to;
  int tonnage;

  // constructor for a carriage
  Carriage(ITravel from, ITravel to, int tonnage) {
    this.from = from;
    this.to = to;
    this.tonnage = tonnage;
  }
}

// examples of a travel mechanic in a game
class ExamplesTravel {
  ExamplesTravel() {
  }

  // examples of housing
  ITravel hovel = new Hut(5, 1);
  ITravel winterfell = new Castle("Winterfell", "Stark", 500, 6);
  ITravel crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  ITravel mudhut = new Hut(4, 3);
  ITravel golden = new Castle("The Golden Palace", "Orion", 1000, 20);
  ITravel traveler = new Inn("The Weary Traveler", 50, 35, 6);

  // examples of travel
  ITravel horse1 = new Horse(this.golden, this.traveler, "Black Bess", "Black");
  ITravel horse2 = new Horse(this.hovel, this.mudhut, "Brown Brian", "Brown");
  ITravel carriage1 = new Carriage(this.traveler, this.golden, 10);
  ITravel carriage2 = new Carriage(this.crossroads, this.winterfell, 20);
}