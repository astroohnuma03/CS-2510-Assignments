// to represent information about a dog
class Dog {
  String name;
  String breed;
  int yob;
  String state;
  boolean hypoallergenic;

  // constructor for the dog class
  Dog(String name, String breed, int yob, String state, boolean hypoallergenic) {
    this.name = name;
    this.breed = breed;
    this.yob = yob;
    this.state = state;
    this.hypoallergenic = hypoallergenic;
  }
}

// example class used to run tests and examples for the dog class
class ExamplesDog {
  ExamplesDog() {
  }
  
  // examples of dogs created using the dog class
  Dog huffle = new Dog("Hufflepuff", "Wheaten Terrier", 2012, "TX", true);
  Dog pearl = new Dog("Pearl", "Labrador Retriever", 2016, "MA", false);
  Dog dusty = new Dog("Dusty", "Mutt", 1995, "NH", false);
}