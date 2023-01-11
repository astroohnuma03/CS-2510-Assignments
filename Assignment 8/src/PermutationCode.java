import tester.*;
import java.util.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random(1);

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> result = new ArrayList<Character>();
    ArrayList<Character> abc = (ArrayList<Character>) alphabet.clone();
    for (int i = 0; i < 26; i = i + 1) {
      result.add(abc.remove(rand.nextInt(26 - i)));
    }

    return result;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    String result = new String("");
    char[] arr = source.toCharArray();
    for (char i : arr) {
      result = result + code.get(alphabet.indexOf(i));
    }

    return result;
  }

  // produce a decoded String from the given String
  String decode(String code) {
    String result = new String("");
    char[] arr = code.toCharArray();
    for (char i : arr) {
      result = result + alphabet.get(this.code.indexOf(i));
    }

    return result;
  }
}

class ExamplesPermutation {

  // code which represents the alphabet with every letter shifted one over
  ArrayList<Character> code1 = new ArrayList<Character>(
      Arrays.asList('b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
          'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'a'));

  // code which represents the alphabet in reverse order
  ArrayList<Character> code2 = new ArrayList<Character>(
      Arrays.asList('z', 'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k',
          'j', 'i', 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'));

  // permutation of code1
  PermutationCode pCode1 = new PermutationCode(code1);

  // permutation of code2
  PermutationCode reverseCode = new PermutationCode(code2);

  // permutation of a random code
  PermutationCode randomCode = new PermutationCode();

  boolean testDecode(Tester t) {
    return t.checkExpect(pCode1.decode("bqqmf"), "apple")
        && t.checkExpect(reverseCode.decode("slfhv"), "house");
  }

  boolean testEncode(Tester t) {
    return t.checkExpect(pCode1.encode("apple"), "bqqmf")
        && t.checkExpect(reverseCode.encode("house"), "slfhv");
  }

  boolean testInitEncoder(Tester t) {
    return t.checkExpect(randomCode.encode("random"), "jriokg")
        && t.checkExpect(randomCode.decode("jriokg"), "random");
  }
}