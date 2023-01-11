import tester.*;
import java.util.Comparator;

// to represent a book and its various fields
class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

// to represent abstract class for BST with leaves and nodes
abstract class ABST<T> {
  Comparator<T> order;

  ABST(Comparator<T> order) {
    this.order = order;
  }

  // returns new BST made from this BST with the given data value
  // inserted in the correct place in it
  public ABST<T> insert(T t) {
    return new Leaf<T>(this.order);
  }

  // returns true if the given item is found in this BST
  public boolean present(T t) {
    return true;
  }

  // returns the left most item in this BST
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // returns the data of the previous ABST if called on a leaf and recursively
  // calls getLeftmost otherwise
  public T getLeftmostHelper(T previous) {
    return previous;
  }

  // returns a new BST made up of the original BST without the leftmost item
  public ABST<T> getRight() {
    return new Leaf<T>(this.order);
  }

  // returns true if this tree and a given tree have the same structure
  // and data based on the comparator
  public boolean sameTree(ABST<T> other) {
    return true;
  }

  // returns false for the abstract class as we can't know if it is a node
  // or not
  public boolean sameNode(Node<T> other) {
    return false;
  }

  // returns false for the abstract class as we can't know if it is a leaf
  // or not
  public boolean sameLeaf(Leaf<T> other) {
    return false;
  }

  // returns true if this tree has the same data in it as the given tree
  public boolean sameData(ABST<T> other) {
    return false;
  }

  // returns true if every item in this tree is in the given tree
  public boolean sameDataHelper(ABST<T> other) {
    return false;
  }

  // returns a list made up of all the items in this BST in order
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

// to represent the leaf of a BST
class Leaf<T> extends ABST<T> {
  Leaf(Comparator<T> order) {
    super(order);
  }

  // returns a new node in the correct place in the original BST
  @Override
  public ABST<T> insert(T t) {
    return new Node<T>(this.order, t, new Leaf<T>(this.order), new Leaf<T>(this.order));
  }

  // returns false if a leaf is reached as this means no matching item was
  // found and the given item was not present in the BST
  @Override
  public boolean present(T t) {
    return false;
  }

  // throws an error if getLeftmost is given an empty tree
  @Override
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // returns the data of the previous node if this node is a leaf
  @Override
  public T getLeftmostHelper(T previous) {
    return previous;
  }

  // returns an error if this BST is empty
  @Override
  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  // returns true if this and the given ABST are both leaves as
  // leaves are automatically the same by default
  @Override
  public boolean sameTree(ABST<T> other) {
    return other.sameLeaf(this);
  }

  // returns true as this is a leaf
  @Override
  public boolean sameLeaf(Leaf<T> other) {
    return true;
  }

  // returns true if every item in this BST is in the given BST and every item
  // in the given BST is in this BST
  @Override
  public boolean sameData(ABST<T> other) {
    return this.sameDataHelper(other) && other.sameDataHelper(this);
  }

  // returns true as two leaves will always be the same
  @Override
  public boolean sameDataHelper(ABST<T> other) {
    return true;
  }

  // returns the empty list if called on a leaf as this means
  // the BST is now empty and we have completed the list
  @Override
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

// to represent the node of a BST
class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  // returns a new BST made up from the original BST with the given data value
  // inserted in the correct place in it
  @Override
  public ABST<T> insert(T t) {
    if (this.order.compare(this.data, t) == 1) {
      return new Node<T>(this.order, this.data, this.left.insert(t), this.right);
    }
    else if (this.order.compare(this.data, t) == -1) {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(t));
    }
    else {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(t));
    }
  }

  // returns true if the given item is found to exist in this BST
  @Override
  public boolean present(T t) {
    if (this.order.compare(this.data, t) == 0) {
      return true;
    }
    else if (this.order.compare(this.data, t) == 1) {
      return this.left.present(t);
    }
    else {
      return this.right.present(t);
    }
  }

  // returns the data of the left most item in this BST
  @Override
  public T getLeftmost() {
    return this.left.getLeftmostHelper(this.data);
  }

  // since this is a node, returns getLeftmost on itself since we need to check
  // if the left of this node is a leaf
  @Override
  public T getLeftmostHelper(T previous) {
    return this.getLeftmost();
  }

  // returns a new BST made from the original BST with the leftmost item removed
  @Override
  public ABST<T> getRight() {
    if (this.order.compare(this.data, this.getLeftmost()) == 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  // returns true if this BST is the same as the given BST
  @Override
  public boolean sameTree(ABST<T> other) {
    return other.sameNode(this);
  }

  // returns true if this node is the same as the given node based on
  // this order
  @Override
  public boolean sameNode(Node<T> other) {
    return this.order.compare(this.data, other.data) == 0 && this.left.sameTree(other.left)
        && this.right.sameTree(other.right);
  }

  // returns true if every item in this BST is in the given BST and every item
  // in the given BST is in this BST
  @Override
  public boolean sameData(ABST<T> other) {
    return this.sameDataHelper(other) && other.sameDataHelper(this);
  }

  // returns true if every item in this BST is in the given BST
  @Override
  public boolean sameDataHelper(ABST<T> other) {
    return other.present(this.data) && this.left.sameDataHelper(other)
        && this.right.sameDataHelper(other);
  }

  // returns a list of every item in this BST in order
  @Override
  public IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), (this.getRight()).buildList());
  }
}

// to represent a generic list
interface IList<T> {
}

// an empty generic list
class MtList<T> implements IList<T> {

}

// a non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

// to represent a class where books are compared by their title
class BooksByTitle implements Comparator<Book> {

  // compares the titles of books and returns a 1 if the given title
  // comes alphabetically before this title, a -1 if the opposite is true,
  // and 0 if the titles are the same
  public int compare(Book t1, Book t2) {
    if (t1.title.compareTo(t2.title) > 0) {
      return 1;
    }
    else if (t1.title.compareTo(t2.title) < 0) {
      return -1;
    }
    else {
      return 0;
    }
  }
}

// to represent a class where books are compared by their author
class BooksByAuthor implements Comparator<Book> {

  // compares the names of authors of books and returns a 1 if the given
  // author's name comes alphabetically before this author's name, a -1 if
  // the opposite is true, and 0 if they have the same name
  public int compare(Book t1, Book t2) {
    if (t1.author.compareTo(t2.author) > 0) {
      return 1;
    }
    else if (t1.author.compareTo(t2.author) < 0) {
      return -1;
    }
    else {
      return 0;
    }
  }
}

// to represent a class where books are compared by their price
class BooksByPrice implements Comparator<Book> {

  // compares the prices of books and returns a 1 if the result is positive
  // -1 if it is negative, or 0 if they are the same price
  public int compare(Book t1, Book t2) {
    if ((t1.price - t2.price) > 0) {
      return 1;
    }
    else if ((t1.price - t2.price) < 0) {
      return -1;
    }
    else {
      return 0;
    }
  }
}

class ExamplesABST {
  Book book1 = new Book("Red Book", "George", 10);
  Book book2 = new Book("Blue Book", "Harry", 12);
  Book book3 = new Book("Green Book", "Bob", 14);
  Book book4 = new Book("Yellow Book", "James", 16);
  Book book5 = new Book("Purple Book", "Adam", 13);

  Book book6 = new Book("Orange Book", "Tim", 8);
  Book book7 = new Book("Brown Book", "Mary", 17);
  Book book8 = new Book("White Book", "John", 22);
  Book book9 = new Book("Black Book", "Kim", 11);

  Book emptyBook = new Book("", "", 0);

  Comparator<Book> price = new BooksByPrice();
  Comparator<Book> author = new BooksByAuthor();
  Comparator<Book> title = new BooksByTitle();

  ABST<Book> leafP = new Leaf<Book>(price);
  ABST<Book> leafA = new Leaf<Book>(author);
  ABST<Book> leafT = new Leaf<Book>(title);

  // binary search tree made up by ordering by price
  ABST<Book> nodeP4 = new Node<Book>(price, book1, leafP, leafP);
  ABST<Book> nodeP3 = new Node<Book>(price, book2, nodeP4, leafP);
  ABST<Book> nodeP2 = new Node<Book>(price, book4, leafP, leafP);
  ABST<Book> nodeP1 = new Node<Book>(price, book3, nodeP3, nodeP2);

  // binary search tree made up by ordering by author
  ABST<Book> nodeA4 = new Node<Book>(author, book3, leafA, leafA);
  ABST<Book> nodeA3 = new Node<Book>(author, book1, nodeA4, leafA);
  ABST<Book> nodeA2 = new Node<Book>(author, book4, leafA, leafA);
  ABST<Book> nodeA1 = new Node<Book>(author, book2, nodeA3, nodeA2);

  // binary search tree made up by ordering by title
  ABST<Book> nodeT4 = new Node<Book>(title, book4, leafT, leafT);
  ABST<Book> nodeT3 = new Node<Book>(title, book1, leafT, nodeT4);
  ABST<Book> nodeT2 = new Node<Book>(title, book2, leafT, leafT);
  ABST<Book> nodeT1 = new Node<Book>(title, book3, nodeT2, nodeT3);

  // binary search tree to represent the result of calling getRight on
  // nodeP1
  ABST<Book> nodeRP3 = new Node<Book>(price, book2, leafP, leafP);
  ABST<Book> nodeRP2 = new Node<Book>(price, book4, leafP, leafP);
  ABST<Book> nodeRP1 = new Node<Book>(price, book3, nodeRP3, nodeRP2);

  // binary search tree to represent the result of calling getRight on
  // nodeP1
  ABST<Book> nodeRA3 = new Node<Book>(author, book1, leafA, leafA);
  ABST<Book> nodeRA2 = new Node<Book>(author, book4, leafA, leafA);
  ABST<Book> nodeRA1 = new Node<Book>(author, book2, nodeRA3, nodeRA2);

  // binary search tree to represent the result of calling getRight on
  // nodeP1
  ABST<Book> nodeRT3 = new Node<Book>(title, book4, leafT, leafT);
  ABST<Book> nodeRT2 = new Node<Book>(title, book1, leafT, nodeRT3);
  ABST<Book> nodeRT1 = new Node<Book>(title, book3, leafT, nodeRT2);

  // binary search tree made up by ordering by price, represents
  // the result of calling insert on nodeP1 when given book5
  ABST<Book> nodePI5 = new Node<Book>(price, book5, leafP, leafP);
  ABST<Book> nodePI4 = new Node<Book>(price, book1, leafP, leafP);
  ABST<Book> nodePI3 = new Node<Book>(price, book2, nodePI4, nodePI5);
  ABST<Book> nodePI2 = new Node<Book>(price, book4, leafP, leafP);
  ABST<Book> nodePI1 = new Node<Book>(price, book3, nodePI3, nodePI2);

  // binary search tree made up by ordering by author, represents
  // the result of calling insert on nodeA1 when given book5
  ABST<Book> nodeAI5 = new Node<Book>(author, book5, leafA, leafA);
  ABST<Book> nodeAI4 = new Node<Book>(author, book3, nodeAI5, leafA);
  ABST<Book> nodeAI3 = new Node<Book>(author, book1, nodeAI4, leafA);
  ABST<Book> nodeAI2 = new Node<Book>(author, book4, leafA, leafA);
  ABST<Book> nodeAI1 = new Node<Book>(author, book2, nodeAI3, nodeAI2);

  // binary search tree made up by ordering by title, represents
  // the result of calling insert on nodeT1 when given book5
  ABST<Book> nodeTI5 = new Node<Book>(title, book5, leafT, leafT);
  ABST<Book> nodeTI4 = new Node<Book>(title, book4, leafT, leafT);
  ABST<Book> nodeTI3 = new Node<Book>(title, book1, nodeTI5, nodeTI4);
  ABST<Book> nodeTI2 = new Node<Book>(title, book2, leafT, leafT);
  ABST<Book> nodeTI1 = new Node<Book>(title, book3, nodeTI2, nodeTI3);

  // binary search tree for testing sameTree with nodeP1
  ABST<Book> nodePT4 = new Node<Book>(price, book1, leafP, leafP);
  ABST<Book> nodePT3 = new Node<Book>(price, book2, nodePT4, leafP);
  ABST<Book> nodePT2 = new Node<Book>(price, book4, leafP, leafP);
  ABST<Book> nodePT1 = new Node<Book>(price, book3, nodePT3, nodePT2);

  // binary search tree for testing sameData with nodeP1
  ABST<Book> nodeD4 = new Node<Book>(price, book3, leafP, leafP);
  ABST<Book> nodeD3 = new Node<Book>(price, book4, nodeD4, leafP);
  ABST<Book> nodeD2 = new Node<Book>(price, book1, leafP, leafP);
  ABST<Book> nodeD1 = new Node<Book>(price, book2, nodeD2, nodeD3);

  // binary search tree representing a larger tree ordered by price
  ABST<Book> nodeLargePrice9 = new Node<Book>(price, book8, leafP, leafP);
  ABST<Book> nodeLargePrice8 = new Node<Book>(price, book6, leafP, leafP);
  ABST<Book> nodeLargePrice7 = new Node<Book>(price, book7, leafP, nodeLargePrice9);
  ABST<Book> nodeLargePrice6 = new Node<Book>(price, book1, nodeLargePrice8, leafP);
  ABST<Book> nodeLargePrice5 = new Node<Book>(price, book3, leafP, leafP);
  ABST<Book> nodeLargePrice4 = new Node<Book>(price, book9, leafP, leafP);
  ABST<Book> nodeLargePrice3 = new Node<Book>(price, book4, nodeLargePrice6, nodeLargePrice7);
  ABST<Book> nodeLargePrice2 = new Node<Book>(price, book2, nodeLargePrice4, nodeLargePrice5);
  ABST<Book> nodeLargePrice1 = new Node<Book>(price, book5, nodeLargePrice2, nodeLargePrice3);

  // list representing nodeP1
  IList<Book> list1 = new ConsList<Book>(book1, new ConsList<Book>(book2,
      new ConsList<Book>(book3, new ConsList<Book>(book4, new MtList<Book>()))));

  // list representing nodeA1
  IList<Book> list2 = new ConsList<Book>(book3, new ConsList<Book>(book1,
      new ConsList<Book>(book2, new ConsList<Book>(book4, new MtList<Book>()))));

  // list representing nodeT1
  IList<Book> list3 = new ConsList<Book>(book2, new ConsList<Book>(book3,
      new ConsList<Book>(book1, new ConsList<Book>(book4, new MtList<Book>()))));

  // list representing nodeLargePrice1
  IList<Book> list4 = new ConsList<Book>(book9,
      new ConsList<Book>(book2,
          new ConsList<Book>(book3, new ConsList<Book>(book5,
              new ConsList<Book>(book6, new ConsList<Book>(book1, new ConsList<Book>(book4,
                  new ConsList<Book>(book7, new ConsList<Book>(book8, new MtList<Book>())))))))));

  public boolean testCompare(Tester t) {
    return t.checkExpect(new BooksByAuthor().compare(book1, book2), -1)
        && t.checkExpect(new BooksByTitle().compare(book1, book2), 1)
        && t.checkExpect(new BooksByPrice().compare(book3, book3), 0);
  }

  public boolean testInsert(Tester t) {
    return t.checkExpect(nodeP1.insert(book5), nodePI1)
        && t.checkExpect(nodeA1.insert(book5), nodeAI1)
        && t.checkExpect(nodeT1.insert(book5), nodeTI1)
        && t.checkExpect(leafP.insert(book5), nodePI5);
  }

  public boolean testPresent(Tester t) {
    return t.checkExpect(nodeP1.present(book2), true) && t.checkExpect(nodeP1.present(book5), false)
        && t.checkExpect(nodeA1.present(book1), true) && t.checkExpect(nodeT1.present(book3), true)
        && t.checkExpect(nodeA1.present(book5), false)
        && t.checkExpect(nodePI1.present(book5), true)
        && t.checkExpect(nodePI1.present(emptyBook), false)
        && t.checkExpect(leafT.present(book1), false);
  }

  public boolean testGetLeftmost(Tester t) {
    return t.checkExpect(nodeP1.getLeftmost(), book1) && t.checkExpect(nodePI1.getLeftmost(), book1)
        && t.checkExpect(nodeA1.getLeftmost(), book3) && t.checkExpect(nodeT1.getLeftmost(), book2)
        && t.checkException(new RuntimeException("No leftmost item of an empty tree"), leafP,
            "getLeftmost");
  }

  public boolean testGetRight(Tester t) {
    return t.checkExpect(nodeP1.getRight(), nodeRP1) && t.checkExpect(nodeA1.getRight(), nodeRA1)
        && t.checkExpect(nodeT1.getRight(), nodeRT1)
        && t.checkException(new RuntimeException("No right of an empty tree"), leafA, "getRight");
  }

  public boolean testSameTree(Tester t) {
    return t.checkExpect(nodeP1.sameTree(nodePT1), true)
        && t.checkExpect(nodeP1.sameTree(nodePI1), false)
        && t.checkExpect(nodeA1.sameTree(nodeT1), false)
        && t.checkExpect(leafP.sameTree(leafP), true);
  }

  public boolean testSameData(Tester t) {
    return t.checkExpect(nodeP1.sameData(nodeD1), true)
        && t.checkExpect(nodeP1.sameData(nodePI1), false)
        && t.checkExpect(nodeA1.sameData(nodeT1), true)
        && t.checkExpect(leafT.sameData(nodeT1), false)
        && t.checkExpect(leafP.sameData(leafP), true);
  }

  public boolean testBuildList(Tester t) {
    return t.checkExpect(nodeP1.buildList(), list1) && t.checkExpect(nodeA1.buildList(), list2)
        && t.checkExpect(nodeT1.buildList(), list3)
        && t.checkExpect(nodeLargePrice1.buildList(), list4)
        && t.checkExpect(leafT.buildList(), new MtList<Book>());
  }
}