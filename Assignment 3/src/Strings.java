// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.combine() ... String
   */

  // create a new list made up of Strings from the original list
  // except sorted in alphabetical order and treated as if they
  // were all in lowercase
  ILoString sort();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.sort() ... ILoString
   * this.rest.sort().insert(String) ... ILoString
   */

  // insert the given string into the list of Strings already
  // sorted alphabetically
  ILoString insert(String s);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.first.compareToIgnoreCase(String) ... int
   * this.rest.insert(String) ... ILoString
   */

  // checks if a list has been sorted alphabetically or not
  boolean isSorted();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.isSorted() ... boolean
   */

  // stores string of the previous entry in the list for comparison
  boolean isSortedHelper(String prev);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.isSortedHelper(String) ... boolean
   */

  // create a list by interweaving the elements of two different list
  ILoString interleave(ILoString other);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * other.interleave(ILoString) ... ILoString
   * (this.interleave(this.reverse())).isDoubledList() ... boolean
   */

  // merges two sorted lists
  ILoString merge(ILoString given);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.merge(ILoString) ... ILoString
   * given.merge(ILoString) ... ILoString
   */
  
  // merges lists together after sorting them
  boolean mergeHelper(String given);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.mergeHelper(String) ... boolean
   */
  
  // reverses the order of the strings in a list
  ILoString reverse();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.reverse() ... ILoString
   * this.rest.reverse().reverseHelper(String) ... ILoString
   * (this.interleave(this.reverse())).isDoubledList() ... boolean
   */

  // places the given string at the end of a list
  ILoString reverseHelper(String last);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.reverse().reverseHelper(String) ... ILoString
   * this.rest.reverseHelper(String) ... ILoString
   */

  // checks if a list is made up of identical pairs of strings
  boolean isDoubledList();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.isDoubledList() ... boolean
   * (this.interleave(this.reverse())).isDoubledList() ... boolean
   */

  // stores string of the previous entry in the list to check
  // for sameness
  boolean isDoubledListHelper(String same);
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * this.rest.isDoubledListHelper(String) ... boolean
   */

  // checks if the list contains the same strings when reading it forwards
  // or backwards
  boolean isPalindromeList();
  /*
   * TEMPLATE
   * Fields on Parameters:
   * this.first ... String
   * this.rest ... ILoString
   * Methods on Parameters:
   * (this.interleave(this.reverse())).isDoubledList() ... boolean
   */
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString() {
  }
  /*
   * TEMPLATE
   * Fields:
   * N/A
   * Methods:
   * this.combine() ... String
   * this.sort() ... ILoString
   * this.insert() ... ILoString
   * this.isSorted() ... boolean
   * this.isSortedHelper(String prev) ... boolean
   * this.interleave(ILoString other) ... ILoString
   * this.merge(ILoString given) ... ILoString
   * this.reverse() ... ILoString
   * this.reverseHelper(String last) ... ILoString
   * this.isDoubledList() ... boolean
   * this.isDoubledListHelper(String same) ... boolean
   * this.isPalindromeList() ... boolean
   * Methods for Fields:
   * N/A
   */

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  // sort a list of strings alphabetically
  public ILoString sort() {
    return this;
  }

  // insert the given string into a list of strings
  // already sorted alphabetically
  public ILoString insert(String s) {
    return new ConsLoString(s, this);
  }

  // if the isSorted method reaches an empty list, the list
  // must be sorted correctly as empty list only appears at the end of a list
  public boolean isSorted() {
    return true;
  }

  // should return true as isSorted should always come first
  // and that will return true with an empty list anyway
  public boolean isSortedHelper(String prev) {
    return true;
  }

  // returns the remaining elements in the longer list
  // if the lists are of unequal length
  public ILoString interleave(ILoString other) {
    return other;
  }

  // returns the given list so it can be merged with the first list
  public ILoString merge(ILoString given) {
    return given;
  }
  
  public boolean mergeHelper(String given) {
    return true;
  }

  // returns empty when it reaches an empty list at the end of a list
  public ILoString reverse() {
    return this;
  }

  // insert the given string into a list of already
  // reversed elements
  public ILoString reverseHelper(String last) {
    return new ConsLoString(last, this);
  }

  // returns true if an empty is reached as that means the list
  // is all doubled
  public boolean isDoubledList() {
    return true;
  }

  // returns true as isDoubledList shoudl always return true first
  public boolean isDoubledListHelper(String same) {
    return false;
  }

  public boolean isPalindromeList() {
    return true;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE FIELDS: 
   * ... this.first ... -- String 
   * ... this.rest ... -- ILoString
   * METHODS 
   * ... this.combine() ... -- String
   * ... this.sort() ... ILoString
   * ... this.insert() ... ILoString
   * ... this.isSorted() ... boolean
   * ... this.isSortedHelper(String prev) ... boolean
   * ... this.interleave(ILoString other) ... ILoString
   * ... this.merge(ILoString given) ... ILoString
   * ... this.reverse() ... ILoString
   * ... this.reverseHelper(String last) ... ILoString
   * ... this.isDoubledList() ... boolean
   * ... this.isDoubledListHelper(String same) ... boolean
   * ... this.isPalindromeList() ... boolean
   * METHODS FOR FIELDS
   * ... this.first.concat(String) ... -- String
   * ... this.first.compareToIgnoreCase(String) ... -- int
   * ... this.first.equals(String) ... boolean
   * ... this.rest.combine() ... -- String
   * ... this.rest.sort() ... ILoString
   * ... this.rest.insert(String) ... ILoString
   * ... this.rest.isSorted() ... boolean
   * ... this.rest.isSortedHelper(String) ... boolean
   * ... other.interleave(ILoString) ... ILoString
   * ... this.rest.merge(ILoString) ... ILoString
   * ... this.rest.reverse() ... ILoString
   * ... this.rest.reverseHelper(String) ... ILoString
   * ... this.rest.isDoubledList() ... boolean
   * ... this.rest.isDoubledListHelper(String) ... boolean
   * ... (this.interleave(this.reverse())).isDoubledList() ... boolean
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }

  // sort all the strings in a list lexicographically
  public ILoString sort() {
    return this.rest.sort().insert(this.first);
  }

  // insert the given string into a list of strings
  // already sorted alphabetically
  public ILoString insert(String s) {
    if (this.first.compareToIgnoreCase(s) < 0) {
      return new ConsLoString(this.first, this.rest.insert(s));
    }
    else {
      return new ConsLoString(s, this);
    }
  }

  // check if this list is sorted or not
  public boolean isSorted() {
    if (this.rest.isSortedHelper(this.first)) {
      return this.rest.isSorted();
    }
    else {
      return false;
    }
  }

  // check if the first element in a list goes alphabetically
  // before the second element, and return true if so
  public boolean isSortedHelper(String prev) {
    return this.first.compareToIgnoreCase(prev) >= 0;
  }

  // create a new list made up of elements from this list and
  // a given list alternating
  public ILoString interleave(ILoString other) {
    return new ConsLoString(this.first, other.interleave(this.rest));
  }
  
  // create a new list by merging two sorted lists
  public ILoString merge(ILoString given) {
    if (given.mergeHelper(this.first)) {
      return new ConsLoString(this.first, this.rest.merge(given));
    }
    else {
      return given.merge(this);
    }
  }
  
  // check if the first element of one list comes before the first
  // element of another list alphabetically
  public boolean mergeHelper(String given) {
    if (this.first.compareToIgnoreCase(given) > 0) {
      return this.rest.mergeHelper(given);
    }
    else {
      return false;
    }
  }
  
  // reverse the order of elements in a list
  public ILoString reverse() {
    return this.rest.reverse().reverseHelper(this.first);
  }

  // insert the first element of a list into the last place
  // in a new reversed list
  public ILoString reverseHelper(String last) {
    return new ConsLoString(this.first, this.rest.reverseHelper(last));
  }

  // check if this list is made up of pairs of the same elements
  public boolean isDoubledList() {
    return this.rest.isDoubledListHelper(this.first);
  }

  // checks if the given first element is equal to the second element
  // in a list and check the rest of the list
  public boolean isDoubledListHelper(String same) {
    return this.first.equals(same) && this.rest.isDoubledList();
  }

  // checks if this list is the same when read forwards or backwards
  public boolean isPalindromeList() {
    return (this.interleave(this.reverse())).isDoubledList();
  }
}

// to represent examples for lists of strings
class ExamplesStrings {

  ILoString mary = new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
      new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))));
  ILoString marySorted = new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("lamb.",
      new ConsLoString("little ", new ConsLoString("Mary ", new MtLoString())))));
  ILoString list1 = new ConsLoString("a",
      new ConsLoString("b", new ConsLoString("c", new ConsLoString("d", new MtLoString()))));
  ILoString list2 = new ConsLoString("e", new ConsLoString("f", new ConsLoString("g",
      new ConsLoString("h", new ConsLoString("i", new ConsLoString("j", new MtLoString()))))));
  ILoString listInterleave = new ConsLoString("a", new ConsLoString("e", new ConsLoString("b",
      new ConsLoString("f",
          new ConsLoString("c", new ConsLoString("g", new ConsLoString("d", new ConsLoString("h",
              new ConsLoString("i", new ConsLoString("j", new MtLoString()))))))))));
  ILoString listMerge = new ConsLoString("a", new ConsLoString("b", new ConsLoString("c",
      new ConsLoString("d",
          new ConsLoString("e", new ConsLoString("f", new ConsLoString("g", new ConsLoString("h",
              new ConsLoString("i", new ConsLoString("j", new MtLoString()))))))))));
  ILoString list1Reverse = new ConsLoString("d",
      new ConsLoString("c", new ConsLoString("b", new ConsLoString("a", new MtLoString()))));
  ILoString listDouble = new ConsLoString("a", new ConsLoString("a", new ConsLoString("b",
      new ConsLoString("b", new ConsLoString("c", new ConsLoString("c", new MtLoString()))))));
  ILoString listPalindrome = new ConsLoString("a",
      new ConsLoString("b", new ConsLoString("c", new ConsLoString("d",
          new ConsLoString("c", new ConsLoString("b", new ConsLoString("a", new MtLoString())))))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
  }

  // test the sort method for the lists of Strings
  boolean testSort(Tester t) {
    return t.checkExpect(this.mary.sort(), this.marySorted);
  }

  boolean testIsSorted(Tester t) {
    return t.checkExpect(this.marySorted.isSorted(), true)
        && t.checkExpect(this.mary.isSorted(), false);
  }

  boolean testInterleave(Tester t) {
    return t.checkExpect(this.list1.interleave(list2), this.listInterleave);
  }

  boolean testMerge(Tester t) {
    return t.checkExpect(this.list2.merge(list1), this.listMerge);
  }

  boolean testReverse(Tester t) {
    return t.checkExpect(this.list1.reverse(), this.list1Reverse);
  }

  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(this.listDouble.isDoubledList(), true)
        && t.checkExpect(this.list1.isDoubledList(), false);
  }

  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(this.listPalindrome.isPalindromeList(), true)
        && t.checkExpect(this.list1.isPalindromeList(), false);
  }
}