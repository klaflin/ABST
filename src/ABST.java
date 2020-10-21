import tester.*;
import java.util.Comparator;

// an abstract class to represent a binary search tree
public abstract class ABST<T> {
  Comparator<T> order;

  ABST(Comparator<T> order) {
    this.order = order;
  }

  // inserts object where it belongs based on comparisons
  abstract ABST<T> insert(T object);

  // is the object given in the BST?
  abstract boolean present(T object);

  // get leftmost object contained in the BST
  abstract T getLeftMost();

  // helper for getting the leftmost object
  abstract T getLeftMostHelper(T data);

  // returns all but the leftmost object in the BST
  abstract ABST<T> getRight();

  // helper for getting all but the leftmost object in BST
  abstract ABST<T> getRightHelper(ABST<T> right, T data);

  // is this ABST<T> the same as the given one?
  abstract boolean sameTree(ABST<T> other);

  // helper for sameTree
  abstract boolean sameTreeHelper(T data, ABST<T> left, ABST<T> right);

  // do the two trees have the same data?
  abstract boolean sameData(ABST<T> other);

  // helper for sameData
  abstract boolean sameDataHelper(T leftMost, ABST<T> right);

  // is this ABST the same leaf as leaf?
  abstract boolean sameLeaf(Leaf<T> leaf);

  // builds a sorted list of the objects in the trees
  abstract IList<T> buildList();
}

//represents a leaf in a binary search tree
class Leaf<T> extends ABST<T> {

  Leaf(Comparator<T> order) {
    super(order);
  }

  // inserts the object into the proper branch of the tree
  // end of the tree, so return the tree with the object as its data
  ABST<T> insert(T object) {
    return new Node<T>(this.order, object, this, this);
  }

  // is the object in the BST?
  // reached the end of the tree so the object is not there
  boolean present(T object) {
    return false;
  }

  // throws an exception because there is no left object in an empty BST
  T getLeftMost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // helper for getLeftMost
  // end of the left side of a node so returns the data
  T getLeftMostHelper(T data) {
    return data;
  }

  // throws an exception because there is no right of an empty BST
  ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  // a helper for getRight
  // reached the leftmost, so return the right data only
  ABST<T> getRightHelper(ABST<T> right, T data) {
    return right;
  }

  // do the two BSTs have the same data and structure?
  boolean sameTree(ABST<T> other) {
    return other.sameLeaf(this);
  }

  // helper for sameTree
  boolean sameTreeHelper(T data, ABST<T> left, ABST<T> right) {
    return false;
  }

  // do the two BSTs have the same data?
  boolean sameData(ABST<T> other) {
    return other.sameLeaf(this);
  }

  // a helper for sameData
  // in this case we are checking to see if a node has the same data as a leaf
  boolean sameDataHelper(T leftMost, ABST<T> right) {
    return false;
  }

  // is the leaf the same as that leaf?
  // is trivially true as leaves have no data
  boolean sameLeaf(Leaf<T> leaf) {
    return true;
  }

  // creates a sorted list of the data in the BST
  IList<T> buildList() {
    return new MtList<T>();
  }
}

//represents a node in a binary search tree
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

  // inserts the object on either the right or left node of the BST
  ABST<T> insert(T object) {
    if (order.compare(this.data, object) > 0) {
      return new Node<T>(this.order, this.data, this.left.insert(object), this.right);
    }
    else {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(object));
    }
  }

  // checks if the object is in the BST
  boolean present(T object) {
    if (order.compare(this.data, object) == 0) {
      return true;
    }
    else if (order.compare(this.data, object) > 0) {
      return this.left.present(object);
    }
    else {
      return this.right.present(object);
    }
  }

  // finds the left most object in a BST
  T getLeftMost() {
    return this.left.getLeftMostHelper(this.data);
  }

  // goes though the left of the BST until a leaf is found
  T getLeftMostHelper(T data) {
    return this.left.getLeftMostHelper(this.data);
  }

  // returns all but the leftmost item in the BST
  ABST<T> getRight() {
    return this.left.getRightHelper(this.right, this.data);
  }

  // helper for getRight
  ABST<T> getRightHelper(ABST<T> right, T data) {
    return new Node<T>(this.order, data, this.getRight(), right);
  }

  // determines if this tree is the same (data & structure) as the other tree
  boolean sameTree(ABST<T> other) {
    return other.sameTreeHelper(this.data, this.left, this.right);
  }

  // helper for sameTree
  boolean sameTreeHelper(T data, ABST<T> left, ABST<T> right) {
    return (this.order.compare(this.data, data) == 0) && this.left.sameTree(left)
        && this.right.sameTree(right);
  }

  // do the two BSTs have the same set of data?
  boolean sameData(ABST<T> other) {
    return other.sameDataHelper(this.getLeftMost(), this.getRight());
  }

  // helper for sameData
  boolean sameDataHelper(T leftMost, ABST<T> right) {
    return (this.order.compare(this.getLeftMost(), leftMost) == 0)
        && this.getRight().sameData(right);
  }

  // is this node the same as that leaf?
  boolean sameLeaf(Leaf<T> leaf) {
    return false;
  }

  // creates a list of data that is sorted
  IList<T> buildList() {
    return new ConsList<T>(this.getLeftMost(), this.getRight().buildList());
  }
}

//a class to represent a book 
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

//to order books by their titles alphabetically
class BooksByTitle implements Comparator<Book> {

  // compares titles of two books alphabetically
  public int compare(Book o1, Book o2) {
    return o1.title.compareTo(o2.title);
  }

}

//to order books by their authors alphabetically
class BooksByAuthor implements Comparator<Book> {

  // compares two books by their authors alphabetically
  public int compare(Book o1, Book o2) {
    return o1.author.compareTo(o2.author);
  }

}

//to order books by their prices low to high
class BooksByPrice implements Comparator<Book> {

  // compares two books by their prices low to high
  public int compare(Book o1, Book o2) {
    return o1.price - o2.price;
  }
}

//interface to represent an arbitrary list of objects
interface IList<T> {
}

//a class to represent an empty list of objects
class MtList<T> implements IList<T> {
}

//a class to represent a non-empty list of objects
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

class ExamplesBST {
  // book examples
  /*
   * books asc by title: hp2, hp4, hp6, hp5, hp3, hp1 = hp7
   * 
   * books asc by author: hp2, hp3 = hp4 = hp6 = hp7, hp1, hp5
   * 
   * books asc by price: hp3, hp1 = hp6, hp4, hp2 = hp5, hp7
   */
  Book harryPotter1 = new Book("Sorcerer's Stone", "Kellie Laflin", 10);
  Book harryPotter2 = new Book("Chamber of Secrets", "Ellie Adams", 12);
  Book harryPotter3 = new Book("Prisoner of Askaban", "JK Rowling", 9);
  // same author as harryPotter3
  Book harryPotter4 = new Book("Goblet of Fire", "JK Rowling", 11);
  // same price as harryPotter2
  Book harryPotter5 = new Book("Order of the Pheonix", "Sally Smith", 12);
  // same price as harryPotter1, same author as harryPotter3, and harryPotter4
  Book harryPotter6 = new Book("Half Blood Price", "JK Rowling", 10);
  // same title as harryPotter1, same author as harryPotter3, harryPotter4, and
  // harryPotter6
  Book harryPotter7 = new Book("Sorcerer's Stone", "JK Rowling", 15);

  // ************
  // leaf examples
  // ************
  // comparator for titles
  Leaf<Book> leafCompTitles = new Leaf<Book>(new BooksByTitle());
  // comparator for authors
  Leaf<Book> leafCompAuthors = new Leaf<Book>(new BooksByAuthor());
  // comparator for prices
  Leaf<Book> leafCompPrices = new Leaf<Book>(new BooksByPrice());

  // ************
  // examples for using title comparator
  // ************
  // nodes with single book and two leaves:
  Node<Book> titleHP1 = new Node<Book>(new BooksByTitle(), this.harryPotter1, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP2 = new Node<Book>(new BooksByTitle(), this.harryPotter2, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP3 = new Node<Book>(new BooksByTitle(), this.harryPotter3, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP4 = new Node<Book>(new BooksByTitle(), this.harryPotter4, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP5 = new Node<Book>(new BooksByTitle(), this.harryPotter5, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP6 = new Node<Book>(new BooksByTitle(), this.harryPotter6, this.leafCompTitles,
      this.leafCompTitles);
  Node<Book> titleHP7 = new Node<Book>(new BooksByTitle(), this.harryPotter7, this.leafCompTitles,
      this.leafCompTitles);

  // nodes with multiple books:
  // the result of inserting harryPotter7 into titleHP1 - two books with the same
  // title
  Node<Book> sameTitle = new Node<Book>(new BooksByTitle(), this.harryPotter1, this.leafCompTitles,
      this.titleHP7);

  // BSTs named:
  // titleHP + <book number at center node> + left <book numbers to the left> +
  // right<book numbers to the right>
  Node<Book> titleHP2right4 = new Node<Book>(new BooksByTitle(), this.harryPotter2,
      this.leafCompTitles, this.titleHP4);
  Node<Book> titleHP6left24right5 = new Node<Book>(new BooksByTitle(), this.harryPotter6,
      this.titleHP2right4, this.titleHP5);
  Node<Book> titleHP3left6245right1 = new Node<Book>(new BooksByTitle(), this.harryPotter3,
      this.titleHP6left24right5, this.titleHP1);

  Node<Book> titleHP6left4right5 = new Node<Book>(new BooksByTitle(), this.harryPotter6,
      this.titleHP4, this.titleHP5);

  // a BST that is the result of removing the left-most node of
  // titleHP3left6245right1
  Node<Book> alltitlesbutleft = new Node<Book>(new BooksByTitle(), this.harryPotter3,
      this.titleHP6left4right5, this.titleHP1);

  // two different valid BST containing the same data with different structures
  Node<Book> titleHP6right5 = new Node<Book>(new BooksByTitle(), this.harryPotter6,
      this.leafCompTitles, this.titleHP5);
  Node<Book> titleHP5left6 = new Node<Book>(new BooksByTitle(), this.harryPotter5, this.titleHP6,
      this.leafCompTitles);

  // ************
  // examples for using author comparator
  // ************
  // nodes with single book and two leaves:
  Node<Book> authorHP1 = new Node<Book>(new BooksByAuthor(), this.harryPotter1,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP2 = new Node<Book>(new BooksByAuthor(), this.harryPotter2,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP3 = new Node<Book>(new BooksByAuthor(), this.harryPotter3,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP4 = new Node<Book>(new BooksByAuthor(), this.harryPotter4,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP5 = new Node<Book>(new BooksByAuthor(), this.harryPotter5,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP6 = new Node<Book>(new BooksByAuthor(), this.harryPotter6,
      this.leafCompAuthors, this.leafCompAuthors);
  Node<Book> authorHP7 = new Node<Book>(new BooksByAuthor(), this.harryPotter7,
      this.leafCompAuthors, this.leafCompAuthors);

  // nodes with multiple books:
  Node<Book> authorHP4left2right5 = new Node<Book>(new BooksByAuthor(), this.harryPotter4,
      this.authorHP2, this.authorHP5);
  Node<Book> authorHP5left6 = new Node<Book>(new BooksByAuthor(), this.harryPotter5, this.authorHP6,
      this.leafCompAuthors);
  // this is the result of inserting hp6 into authorHP4left2right5 (same Author)
  Node<Book> authorHP4left2right56 = new Node<Book>(new BooksByAuthor(), this.harryPotter4,
      this.authorHP2, this.authorHP5left6);

  // ************
  // examples for using price comparator
  // ************
  // nodes with single book and two leaves:
  Node<Book> priceHP1 = new Node<Book>(new BooksByPrice(), this.harryPotter1, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP2 = new Node<Book>(new BooksByPrice(), this.harryPotter2, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP3 = new Node<Book>(new BooksByPrice(), this.harryPotter3, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP4 = new Node<Book>(new BooksByPrice(), this.harryPotter4, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP5 = new Node<Book>(new BooksByPrice(), this.harryPotter5, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP6 = new Node<Book>(new BooksByPrice(), this.harryPotter6, this.leafCompPrices,
      this.leafCompPrices);
  Node<Book> priceHP7 = new Node<Book>(new BooksByPrice(), this.harryPotter7, this.leafCompPrices,
      this.leafCompPrices);

  // nodes with multiple books
  Node<Book> priceHP1left3right4 = new Node<Book>(new BooksByPrice(), this.harryPotter1,
      this.priceHP3, this.priceHP4);
  Node<Book> priceHP4left6 = new Node<Book>(new BooksByPrice(), this.harryPotter4, this.priceHP6,
      this.leafCompPrices);
  Node<Book> priceHP3right3 = new Node<Book>(new BooksByPrice(), this.harryPotter3,
      this.leafCompPrices, this.priceHP3);
  // the result of inserting hp3 into priceHP1left3right4
  Node<Book> priceHP1left33right4 = new Node<Book>(new BooksByPrice(), this.harryPotter1,
      this.priceHP3right3, this.priceHP4);
  // the result of inserting hp6 into priceHP1left3right4
  Node<Book> priceHP1left3right46 = new Node<Book>(new BooksByPrice(), this.harryPotter1,
      this.priceHP3, this.priceHP4left6);

  Node<Book> priceHP4left6right2 = new Node<Book>(new BooksByPrice(), this.harryPotter4,
      this.priceHP6, this.priceHP2);
  Node<Book> priceHP6left3 = new Node<Book>(new BooksByPrice(), this.harryPotter6, this.priceHP3,
      this.leafCompPrices);
  // the result of inserting hp3 into priceHP4left6right2 (less than)
  Node<Book> priceHP4left63right2 = new Node<Book>(new BooksByPrice(), this.harryPotter4,
      this.priceHP6left3, this.priceHP2);

  // ************
  // list of book examples
  // ************
  IList<Book> mt = new MtList<Book>();
  IList<Book> hpList65 = new ConsList<Book>(this.harryPotter6,
      new ConsList<Book>(this.harryPotter5, this.mt));
  IList<Book> hpList642 = new ConsList<Book>(this.harryPotter6,
      new ConsList<Book>(this.harryPotter4, new ConsList<Book>(this.harryPotter2, this.mt)));
  IList<Book> hpList2465 = new ConsList<Book>(this.harryPotter2,
      new ConsList<Book>(this.harryPotter4,
          new ConsList<Book>(this.harryPotter6, new ConsList<Book>(this.harryPotter5, this.mt))));
  IList<Book> hpListallbytitle = new ConsList<Book>(this.harryPotter2, new ConsList<Book>(
      this.harryPotter4, new ConsList<Book>(this.harryPotter6, new ConsList<Book>(this.harryPotter5,
          new ConsList<Book>(this.harryPotter3, new ConsList<Book>(this.harryPotter1, this.mt))))));

  /*
   * TESTS FOR COMPARATOR<BOOK> METHODS
   */
  // tests for BooksByTitle()
  boolean testByTitle(Tester t) {
    return t.checkExpect(new BooksByTitle().compare(this.harryPotter2, this.harryPotter4), -4)
        && t.checkExpect(new BooksByTitle().compare(this.harryPotter1, this.harryPotter7), 0)
        && t.checkExpect(new BooksByTitle().compare(this.harryPotter1, this.harryPotter2), 16);
  }

  // tests for BooksByAuthor()
  boolean testByAuthor(Tester t) {
    return t.checkExpect(new BooksByAuthor().compare(this.harryPotter2, this.harryPotter4), -5)
        && t.checkExpect(new BooksByAuthor().compare(this.harryPotter4, this.harryPotter7), 0)
        && t.checkExpect(new BooksByAuthor().compare(this.harryPotter1, this.harryPotter4), 1);
  }

  // tests for BooksByPrice()
  boolean testByPrice(Tester t) {
    return t.checkExpect(new BooksByPrice().compare(this.harryPotter3, this.harryPotter1), -1)
        && t.checkExpect(new BooksByPrice().compare(this.harryPotter1, this.harryPotter6), 0)
        && t.checkExpect(new BooksByPrice().compare(this.harryPotter4, this.harryPotter6), 1);
  }

  /*
   * TESTS FOR ABST<BOOK> METHODS
   */
  // tests for the insert method
  boolean testInsert(Tester t) {
    return t.checkExpect(this.titleHP1.insert(this.harryPotter7), this.sameTitle)
        && t.checkExpect(this.priceHP4left6right2.insert(this.harryPotter3),
            this.priceHP4left63right2)
        && t.checkExpect(this.priceHP1left3right4.insert(this.harryPotter3),
            this.priceHP1left33right4)
        && t.checkExpect(this.priceHP1left3right4.insert(this.harryPotter6),
            this.priceHP1left3right46)
        && t.checkExpect(this.authorHP4left2right5.insert(this.harryPotter6),
            this.authorHP4left2right56)
        && t.checkExpect(this.leafCompPrices.insert(this.harryPotter1), this.priceHP1);
  }

  // tests for present method
  boolean testPresent(Tester t) {
    return t.checkExpect(this.leafCompAuthors.present(this.harryPotter1), false)
        && t.checkExpect(this.authorHP4left2right5.present(this.harryPotter2), true)
        && t.checkExpect(this.priceHP1left3right46.present(this.harryPotter2), false);
  }

  // tests for getLeftMost method
  boolean testGetLeftMost(Tester t) {
    return t.checkExpect(this.titleHP3left6245right1.getLeftMost(), this.harryPotter2)
        && t.checkExpect(this.priceHP1.getLeftMost(), this.harryPotter1)
        && t.checkExpect(this.titleHP2right4.getLeftMost(), this.harryPotter2)
        && t.checkException(new RuntimeException("No leftmost item of an empty tree"),
            this.leafCompAuthors, "getLeftMost");
  }

  // tests for getLeftMostHelper method
  boolean testGetLeftMostHelper(Tester t) {
    return t.checkExpect(this.titleHP6left24right5.getLeftMostHelper(this.harryPotter3),
        this.harryPotter2)
        && t.checkExpect(this.leafCompPrices.getLeftMostHelper(this.harryPotter1),
            this.harryPotter1)
        && t.checkExpect(this.priceHP1left33right4.getLeftMostHelper(this.harryPotter7),
            this.harryPotter3);
  }

  // tests for getRight
  boolean testGetRight(Tester t) {
    return t.checkExpect(this.titleHP6left4right5.getRight(), this.titleHP6right5)
        && t.checkExpect(this.titleHP7.getRight(), this.leafCompTitles)
        && t.checkExpect(this.titleHP3left6245right1.getRight(), this.alltitlesbutleft)
        && t.checkExpect(this.priceHP1left33right4.getRight(), this.priceHP1left3right4)
        && t.checkException(new RuntimeException("No right of an empty tree"), this.leafCompAuthors,
            "getRight");
  }

  // tests for getRightHelper
  boolean testGetRightHelper(Tester t) {
    return t.checkExpect(
        this.leafCompAuthors.getRightHelper(this.authorHP4left2right5, this.harryPotter1),
        this.authorHP4left2right5)
        && t.checkExpect(this.priceHP2.getRightHelper(this.priceHP3, this.harryPotter3),
            this.priceHP3right3)
        && t.checkExpect(this.titleHP5left6.getRightHelper(this.leafCompTitles, this.harryPotter3),
            new Node<Book>(new BooksByTitle(), this.harryPotter3, this.titleHP5,
                this.leafCompTitles));
  }

  // tests sameTree
  boolean testSameTree(Tester t) {
    return t.checkExpect(this.leafCompAuthors.sameTree(this.leafCompAuthors), true)
        && t.checkExpect(this.leafCompAuthors.sameTree(this.titleHP1), false)
        && t.checkExpect(this.titleHP3left6245right1.sameTree(this.leafCompAuthors), false)
        && t.checkExpect(this.titleHP6right5.sameTree(this.titleHP6right5), true)
        && t.checkExpect(this.titleHP5left6.sameTree(this.titleHP6right5), false)
        && t.checkExpect(this.priceHP1left3right4.sameTree(this.titleHP6left4right5), false)
        && t.checkExpect(this.priceHP1left3right4.sameTree(this.priceHP1left3right4), true);
  }

  // tests sameTreeHelper
  boolean testSameTreeHelper(Tester t) {
    return t
        .checkExpect(this.titleHP6right5.sameTreeHelper(this.harryPotter6, this.titleHP6right5.left,
            this.titleHP6right5.right), true)
        && t.checkExpect(this.authorHP4.sameTreeHelper(this.harryPotter4, this.leafCompAuthors,
            this.leafCompPrices), true)
        && t.checkExpect(
            this.alltitlesbutleft.sameTreeHelper(this.harryPotter1, this.authorHP7, this.authorHP3),
            false)
        && t.checkExpect(this.leafCompAuthors.sameTreeHelper(this.harryPotter7,
            this.leafCompAuthors, this.leafCompAuthors), false)
        && t.checkExpect(this.leafCompTitles.sameTreeHelper(this.harryPotter2, this.leafCompTitles,
            this.leafCompTitles), false);
  }

  // tests sameData
  boolean testSameData(Tester t) {
    return t.checkExpect(this.leafCompAuthors.sameData(this.leafCompAuthors), true)
        && t.checkExpect(this.leafCompAuthors.sameData(this.titleHP1), false)
        && t.checkExpect(this.titleHP3left6245right1.sameData(this.leafCompAuthors), false)
        && t.checkExpect(this.titleHP5left6.sameData(this.titleHP6right5), true)
        && t.checkExpect(this.titleHP6right5.sameData(this.titleHP6right5), true)
        && t.checkExpect(this.leafCompAuthors.sameData(this.alltitlesbutleft), false);
  }

  // tests sameDataHelper
  boolean testSameDataHelper(Tester t) {
    return t.checkExpect(
        this.leafCompAuthors.sameDataHelper(this.harryPotter1, this.leafCompAuthors), false)
        && t.checkExpect(
            this.titleHP3left6245right1.sameDataHelper(this.harryPotter2, this.alltitlesbutleft),
            true)
        && t.checkExpect(this.authorHP1.sameDataHelper(this.harryPotter1, this.leafCompAuthors),
            true)
        && t.checkExpect(this.authorHP4.sameDataHelper(this.harryPotter4, this.leafCompPrices),
            true)
        && t.checkExpect(this.authorHP5left6.sameDataHelper(this.harryPotter5, this.authorHP6),
            false)
        && t.checkExpect(this.titleHP6right5.sameDataHelper(this.harryPotter6, this.titleHP5), true)
        && t.checkExpect(this.authorHP5left6.sameDataHelper(this.harryPotter6, this.authorHP5),
            true);
  }

  // tests buildList
  boolean testBuildList(Tester t) {
    return t.checkExpect(this.leafCompAuthors.buildList(), this.mt)
        && t.checkExpect(this.titleHP5left6.buildList(), this.hpList65)
        && t.checkExpect(this.titleHP6right5.buildList(), this.hpList65)
        && t.checkExpect(this.priceHP4left6right2.buildList(), this.hpList642)
        && t.checkExpect(this.authorHP4left2right56.buildList(), this.hpList2465)
        && t.checkExpect(this.titleHP3left6245right1.buildList(), this.hpListallbytitle);
  }

  // tests sameLeaf
  boolean testSameLeaf(Tester t) {
    return t.checkExpect(this.leafCompTitles.sameLeaf(this.leafCompAuthors), true)
        && t.checkExpect(this.leafCompAuthors.sameLeaf(this.leafCompAuthors), true)
        && t.checkExpect(this.authorHP1.sameLeaf(this.leafCompPrices), false)
        && t.checkExpect(this.titleHP3left6245right1.sameLeaf(this.leafCompTitles), false);
  }

}