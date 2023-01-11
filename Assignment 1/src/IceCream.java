// interface that represents ice cream
interface IIceCream {
}

// class that represents ice cream being served in either a cone or a cup
class EmptyServing implements IIceCream {
  boolean cone;

  // constructor for the empty-serving class
  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

// class that represents a flavor of an ice cream scoop and possible other scoops
class Scooped implements IIceCream {
  IIceCream more;
  String flavor;

  // constructor for the scooped class
  Scooped(IIceCream more, String flavor) {
    this.more = more;
    this.flavor = flavor;
  }
}

// examples for the ice cream interface
class ExamplesIceCream {
  ExamplesIceCream() {
  }

  // examples of ice cream in a cone or a cup
  IIceCream cup = new EmptyServing(false);
  IIceCream cone = new EmptyServing(true);

  // examples of different flavors of ice cream
  IIceCream caramel = new Scooped(this.cup, "caramel swirl");
  IIceCream raspberry = new Scooped(this.caramel, "black raspberry");
  IIceCream coffee = new Scooped(this.raspberry, "coffee");
  IIceCream strawberry = new Scooped(this.cone, "strawberry");
  IIceCream vanilla = new Scooped(this.strawberry, "vanilla");

  // examples of different ice cream orders
  IIceCream order1 = new Scooped(this.coffee, "mint chip");
  IIceCream order2 = new Scooped(this.vanilla, "chocolate");
}