import tester.*;
import java.util.function.Predicate;

// to represent a double-ended queue
class Deque<T> {
  Sentinel<T> header;

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  Deque() {
    this.header = new Sentinel<T>();
  }

  // returns the count of how many nodes exist in this deque
  public int size() {
    return this.header.sizeHelper(0);
  }

  // mutates this deque to add a new node at the head of the list
  void addAtHead(T node) {
    new Node<T>(node, this.header.next, this.header);
  }

  // mutates this deque to add a new node at the tail of the list
  void addAtTail(T node) {
    new Node<T>(node, this.header, this.header.prev);
  }

  // mutates this deque to remove the node at the head of the list
  T removeFromHead() {
    return this.header.next.remove();
  }

  // mutates this deque to remove the node at the tail of the list
  T removeFromTail() {
    return this.header.prev.remove();
  }

  // returns the first node in this deque that satisfies the given
  // predicate
  ANode<T> find(Predicate<T> pred) {
    return this.header.next.findData(pred);
  }

  // removes the given node from this deque, does nothing if
  // the given node is the sentinel header
  void removeNode(ANode<T> node) {
    if (header != node) {
      node.remove();
    }
  }
}

// to represent the abstract class for nodes
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // returns 0 once this ANode<T> is reached twice, otherwise calls itself
  // on the next node while adding 1 to the count
  public int sizeHelper(int count) {
    if (count > 0) {
      return 0;
    }
    else {
      return this.next.sizeHelper(count + 1);
    }
  }

  // removes this node from the deque and returns it
  public abstract T remove();

  // returns itself to represent the predicate reaching the header without
  // returning true for any of the nodes in the deque
  public ANode<T> findData(Predicate<T> pred) {
    return this;
  }
}

// to represent the constant sentinel node class
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  // returns 0 once this sentinel is reached twice, otherwise calls itself
  // on the next node while adding 1 to the count
  public int sizeHelper(int count) {
    if (count > 0) {
      return 0;
    }
    else {
      return this.next.sizeHelper(count + 1);
    }
  }

  // returns itself to represent the predicate reaching the header without
  // returning true for any of the nodes in the deque
  public ANode<T> findData(Predicate<T> pred) {
    return this;
  }

  // throws a runtimeException as if remove is called on a sentinel
  // the deque must be empty, and nothing can be removed from an empty list
  @Override
  public T remove() {
    throw new RuntimeException("Nothing to remove from an empty list");
  }
}

// to represent a node in a queue
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    super(null, null);
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;
    this.next = next;
    this.prev = prev;
    this.next.prev = this;
    this.prev.next = this;

    if (this.next == null) {
      throw new IllegalArgumentException("The given next node is null");
    }
    else {
      this.next = next;
    }

    if (this.prev == null) {
      throw new IllegalArgumentException("The given prev node is null");
    }
    else {
      this.prev = prev;
    }
  }

  // calls sizeHelper on this next and adds 1 to itself
  @Override
  public int sizeHelper(int count) {
    return this.next.sizeHelper(count) + 1;
  }

  // returns itself if the given predicate returns true for this data
  // otherwise, calls findData on the next
  @Override
  public ANode<T> findData(Predicate<T> pred) {
    if (pred.test(data)) {
      return this;
    }
    else {
      return next.findData(pred);
    }
  }

  // removes this node from the deque
  @Override
  public T remove() {
    this.prev.next = next;
    this.next.prev = prev;
    return this.data;
  }
}

// to represent examples of double-ended queues
class ExamplesDeque {

  // represents the initial data for the examples to be set back to
  // at the beginning of every new test
  void initData() {

    // represents the empty double-ended queue
    deque1 = new Deque<String>();

    sent1 = new Sentinel<String>();

    node1 = new Node<String>("abc", sent1, sent1);
    node2 = new Node<String>("bcd", sent1, node1);
    node3 = new Node<String>("cde", sent1, node2);
    node4 = new Node<String>("def", sent1, node3);

    deque2 = new Deque<String>(sent1);

    sent2 = new Sentinel<String>();

    node5 = new Node<String>("red", sent2, sent2);
    node6 = new Node<String>("green", sent2, node5);
    node7 = new Node<String>("blue", sent2, node6);
    node8 = new Node<String>("yellow", sent2, node7);
    node9 = new Node<String>("orange", sent2, node8);
    node10 = new Node<String>("purple", sent2, node9);

    deque3 = new Deque<String>(sent2);
  }

  Node<String> node1;
  Node<String> node2;
  Node<String> node3;
  Node<String> node4;
  Node<String> node5;
  Node<String> node6;
  Node<String> node7;
  Node<String> node8;
  Node<String> node9;
  Node<String> node10;

  Sentinel<String> sent1;
  Sentinel<String> sent2;

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;

  public boolean testSize(Tester t) {
    this.initData();
    return t.checkExpect(deque1.size(), 0) && t.checkExpect(deque2.size(), 4)
        && t.checkExpect(deque3.size(), 6);
  }

  public boolean testAddAtHead(Tester t) {
    this.initData();
    deque1.addAtHead("empty");
    deque2.addAtHead("a");
    deque3.addAtHead("white");
    return t.checkExpect(deque1.header.next,
        new Node<String>("empty", deque1.header, deque1.header))
        && t.checkExpect(sent1.next, new Node<String>("a", node1, sent1))
        && t.checkExpect(sent2.next, new Node<String>("white", node5, sent2))
        && t.checkExpect(deque1.size(), 1) && t.checkExpect(deque2.size(), 5)
        && t.checkExpect(deque3.size(), 7);
  }

  public boolean testAddAtTail(Tester t) {
    this.initData();
    deque1.addAtTail("empty");
    deque2.addAtTail("z");
    deque3.addAtTail("black");
    return t.checkExpect(deque1.header.prev,
        new Node<String>("empty", deque1.header, deque1.header))
        && t.checkExpect(sent1.prev, new Node<String>("z", sent1, node4))
        && t.checkExpect(sent2.prev, new Node<String>("black", sent2, node10))
        && t.checkExpect(deque1.size(), 1) && t.checkExpect(deque2.size(), 5)
        && t.checkExpect(deque3.size(), 7);
  }

  public boolean testRemoveFromHead(Tester t) {
    this.initData();
    return t.checkExpect(deque2.removeFromHead(), "abc")
        && t.checkExpect(deque3.removeFromHead(), "red") && t.checkExpect(sent1.next, node2)
        && t.checkExpect(sent2.next, node6) && t.checkExpect(node2.prev, sent1)
        && t.checkExpect(node6.prev, sent2) && t.checkExpect(deque2.size(), 3)
        && t.checkExpect(deque3.size(), 5) && t.checkException(
            new RuntimeException("Nothing to remove from an empty list"), deque1, "removeFromHead");
  }

  public boolean testRemoveFromTail(Tester t) {
    this.initData();
    return t.checkExpect(deque2.removeFromTail(), "def")
        && t.checkExpect(deque3.removeFromTail(), "purple") && t.checkExpect(sent1.prev, node3)
        && t.checkExpect(sent2.prev, node9) && t.checkExpect(node3.next, sent1)
        && t.checkExpect(node9.next, sent2) && t.checkExpect(deque2.size(), 3)
        && t.checkExpect(deque3.size(), 5) && t.checkException(
            new RuntimeException("Nothing to remove from an empty list"), deque1, "removeFromTail");
  }

  public boolean testFind(Tester t) {
    this.initData();
    return t.checkExpect(deque2.find(i -> i.equals("cde")), node3)
        && t.checkExpect(deque2.find(i -> i.equals("bcd")), node2)
        && t.checkExpect(deque2.find(i -> i.equals("a")), sent1)
        && t.checkExpect(deque3.find(i -> i.equals("red")), node5)
        && t.checkExpect(deque3.find(i -> i.equals("yellow")), node8)
        && t.checkExpect(deque3.find(i -> i.equals("green")), node6)
        && t.checkExpect(deque3.find(i -> i.equals("purple")), node10)
        && t.checkExpect(deque3.find(i -> i.equals("black")), sent2);
  }

  public boolean testRemoveNode(Tester t) {
    this.initData();
    deque2.removeNode(sent1);
    deque2.removeNode(node1);
    deque2.removeNode(node3);
    deque3.removeNode(sent2);
    deque3.removeNode(node5);
    deque3.removeNode(node6);
    deque3.removeNode(node8);
    deque3.removeNode(node9);
    return t.checkExpect(deque2.header, sent1) && t.checkExpect(sent1.next, node2)
        && t.checkExpect(deque2.find(i -> i.equals("cde")), sent1)
        && t.checkExpect(deque3.header, sent2) && t.checkExpect(sent2.next, node7)
        && t.checkExpect(sent2.prev, node10)
        && t.checkExpect(deque3.find(i -> i.equals("yellow")), sent2)
        && t.checkExpect(deque3.find(i -> i.equals("orange")), sent2);
  }
}