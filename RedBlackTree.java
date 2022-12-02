import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Stack;

public class RedBlackTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {
  protected static class Node<T> {
    public T data;
    public Node<T> parent;
    public Node<T> leftChild;
    public Node<T> rightChild;
    public int blackHeight;

    public Node(T data) {
      this.data = data;
      this.blackHeight = 0;
    }

    /**
     * Checks if a node is a left child
     * 
     * @return true when this node has a parent and is the left child of that parent, otherwise
     *         return false
     */
    public boolean isLeftChild() {
      return parent != null && parent.leftChild == this;
    }
  }

  protected Node<T> root;
  protected int size = 0;

  /**
   * Performs a naive insertion into a binary search tree: adding the input data value to a new node
   * in a leaf position within the tree. After this insertion, no attempt is made to restructure or
   * balance the tree. This tree will not hold null references, nor duplicate data values.
   * 
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if not
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when the newNode and subtree contain equal data references
   */
  public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");

    Node<T> newNode = new Node<>(data);
    if (root == null) {
      root = newNode;
      size++;
      return true;
    } else {
      boolean returnValue = insertHelper(newNode, root); // recursively insert into subtree
      if (returnValue)
        size++;
      else {
        throw new IllegalArgumentException("This RedBlackTree already contains that value.");
      }
      root.blackHeight = 1;
      return returnValue;
    }
  }

  /**
   * Recursive helper method to find the subtree with a null reference in the position that the
   * newNode should be inserted, and then extend this tree by the newNode in that position.
   * 
   * @param newNode is the new node that is being added to this tree
   * @param subtree is the reference to a node within this tree which the newNode should be inserted
   *                as a descenedent beneath
   * @return true is the value was inserted in subtree, false if not
   */
  private boolean insertHelper(Node<T> newNode, Node<T> subtree) {
    int compare = newNode.data.compareTo(subtree.data);
    // do not allow duplicate values to be stored within this tree
    if (compare == 0) {
      return false;
    }

    else if (compare < 0) {
      if (subtree.leftChild == null) {
        subtree.leftChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode);
        return true;
      } else
        return insertHelper(newNode, subtree.leftChild);
    }

    else {
      if (subtree.rightChild == null) {
        subtree.rightChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode);
        return true;
      } else
        return insertHelper(newNode, subtree.rightChild);
    }
  }

  /**
   * Checks if Red Black Tree properties are satisfied, working it's way up to the root node
   *
   * @param node The newly added node that may violate the Red Black Tree property
   */
  protected void enforceRBTreePropertiesAfterInsert(final Node<T> node) {
    if (node == null || node.parent == null)
      return;

    if (node.blackHeight == 0 && node.parent.blackHeight == 0) {
      propertiesHelper(node);
    }
    enforceRBTreePropertiesAfterInsert(node.parent);
  }

  /**
   * Helper method for enforceRBTreePropertiesAfterInsert
   * 
   * @param node The newly added node that may violate the Red Black Tree property
   */
  private final void propertiesHelper(final Node<T> node) {
    if (node.parent == null || node.parent.parent == null)
      return;

    final Node<T> parent = node.parent;
    final Node<T> grandParent = parent.parent;
    final Node<T> uncle = parent.isLeftChild() ? grandParent.rightChild : grandParent.leftChild;

    final int type = (uncle != null && uncle.blackHeight == 0) ? 3
        : (parent.isLeftChild() == node.isLeftChild()) ? 1 : 2;

    switch (type) {
      case 1:
        handleCase1(parent, grandParent);
        break;

      case 2:
        handleCase2(node, parent, grandParent);
        break;

      case 3:
        handleCase3(parent, grandParent, uncle);
        break;
    }
  }

  /**
   * Handles the RBTree violation where the parent's sibling is black and on the opposite side to
   * the violating node. Algorithm: Rotate the parent and grandparent of the violating node Switch
   * the color of the parent to black and grandparent to red
   *
   * @param node   the node to rotate
   * @param parent the parent of the node to rotate
   */
  private void handleCase1(final Node<T> node, final Node<T> parent) {
    // Rotate the violating nodes parent and grandparent
    rotate(node, parent);

    // Switch colors
    node.blackHeight = 1;
    parent.blackHeight = 0;
  }

  /**
   * Handles the RBTree violation where the parent's sibling is black and on the same side to the
   * violating node Algorithm: Rotate the violating node and it's parent (this creates the case 1
   * scenario again, so handle it like case 1) Handle it like case 1 i.e. Rotate the (original)
   * violating node with the grandparent Switch colors
   *
   * @param node        The violating node
   * @param parent      The parent of the violating node
   * @param grandParent The grandparent of the violating node
   */
  private void handleCase2(final Node<T> node, final Node<T> parent, final Node<T> grandParent) {
    // Switch to case 1
    rotate(node, parent);

    // Handle case 1
    handleCase1(node, grandParent);
  }

  /**
   * Handles the RBTree violation where the parent's sibling is red Algorithm: Set the grandparent
   * to red Set the parent to black Set the uncle to black
   *
   * @param parent      Parent of the violating node
   * @param grandParent GrandParent of the violating node
   * @param uncle       uncle of the violating node
   */
  private void handleCase3(final Node<T> parent, final Node<T> grandParent, final Node<T> uncle) {
    grandParent.blackHeight = 0;
    parent.blackHeight = 1;
    uncle.blackHeight = 1;
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a leftChild of the provided parent, this method will perform a right rotation. When the
   * provided child is a rightChild of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will throw
   * an IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
    if (child.parent != parent || (parent.leftChild != child && parent.rightChild != child)) {
      throw new IllegalArgumentException("incorrect relation");
    }
    if (child.isLeftChild()) {
      rightRotation(child, parent);
    } else {
      leftRotation(child, parent);
    }
  }

  /**
   * Left rotation method
   */
  private void leftRotation(Node<T> child, Node<T> parent) {
    try {
      Node<T> grandChild = child.leftChild;
      parent.rightChild = grandChild;
      if (grandChild != null) {
        grandChild.parent = parent;
      }
      child.parent = parent.parent;
      if (parent.parent == null) {
        root = child;
      } else if (parent.isLeftChild()) {
        parent.parent.leftChild = child;
      } else {
        parent.parent.rightChild = child;
      }

      child.leftChild = parent;
      parent.parent = child;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Left rotation method
   */
  private void rightRotation(Node<T> child, Node<T> parent) {
    try {
      Node<T> grandChild = child.rightChild;
      parent.leftChild = grandChild;
      if (grandChild != null) {
        grandChild.parent = parent;
      }

      child.parent = parent.parent;
      if (parent.parent == null) {
        root = child;
      } else if (parent.isLeftChild()) {
        parent.parent.leftChild = child;
      } else {
        parent.parent.rightChild = child;
      }

      child.rightChild = parent;
      parent.parent = child;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Get the size of the tree (its number of nodes).
   * 
   * @return the number of nodes in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * 
   * @return true of this.size() return 0, false if this.size() > 0
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Checks whether the tree contains the value *data*.
   * 
   * @param data the data value to test for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  public boolean contains(T data) {
    // null references will not be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");
    return this.containsHelper(data, root);
  }

  /**
   * Recursive helper method that recurses through the tree and looks for the value *data*.
   * 
   * @param data    the data value to look for
   * @param subtree the subtree to search through
   * @return true of the value is in the subtree, false if not
   */
  private boolean containsHelper(T data, Node<T> subtree) {
    if (subtree == null) {
      // we are at a null child, value is not in tree
      return false;
    } else {
      int compare = data.compareTo(subtree.data);
      if (compare < 0) {
        // go left in the tree
        return containsHelper(data, subtree.leftChild);
      } else if (compare > 0) {
        // go right in the tree
        return containsHelper(data, subtree.rightChild);
      } else {
        // we found it :)
        return true;
      }
    }
  }

  /**
   * This method performs an inorder traversal of the tree. The string representations of each data
   * value within this tree are assembled into a comma separated string within brackets (similar to
   * many implementations of java.util.Collection, like java.util.ArrayList, LinkedList, etc). Note
   * that this RedBlackTree class implementation of toString generates an inorder traversal. The
   * toString of the Node class class above produces a level order traversal of the nodes / values
   * of the tree.
   * 
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    sb.append(toInOrderStringHelper("", this.root));
    if (this.root != null) {
      sb.setLength(sb.length() - 2);
    }
    sb.append(" ]");
    return sb.toString();
  }

  private String toInOrderStringHelper(String str, Node<T> node) {
    if (node == null) {
      return str;
    }
    str = toInOrderStringHelper(str, node.leftChild);
    str += (node.data.toString() + "-" + node.blackHeight + ", ");
    str = toInOrderStringHelper(str, node.rightChild);
    return str;
  }

  /**
   * This method performs a level order traversal of the tree rooted at the current node. The string
   * representations of each data value within this tree are assembled into a comma separated string
   * within brackets (similar to many implementations of java.util.Collection). Note that the Node's
   * implementation of toString generates a level order traversal. The toString of the RedBlackTree
   * class below produces an inorder traversal of the nodes / values of the tree. This method will
   * be helpful as a helper for the debugging and testing of your rotation implementation.
   * 
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    String output = "[ ";
    if (this.root != null) {
      LinkedList<Node<T>> q = new LinkedList<>();
      q.add(this.root);
      while (!q.isEmpty()) {
        Node<T> next = q.removeFirst();
        if (next.leftChild != null)
          q.add(next.leftChild);
        if (next.rightChild != null)
          q.add(next.rightChild);
        output += next.data.toString();
        if (!q.isEmpty())
          output += ", ";
      }
    }
    return output + " ]";
  }

  public String toString() {
    return "level order: " + this.toLevelOrderString() + "\nin order: " + this.toInOrderString();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      Stack<Node<T>> stack = null;
      Node<T> current = root;

      public T next() {
        if (stack == null) {
          stack = new Stack<Node<T>>();
          current = root;
        }
        while (current != null) {
          stack.push(current);
          current = current.leftChild;
        }
        if (!stack.isEmpty()) {
          Node<T> processedNode = stack.pop();
          current = processedNode.rightChild;
          return processedNode.data;
        } else {
          throw new NoSuchElementException("There are no more elements in the tree");
        }

      }

      public boolean hasNext() {
        return !(current == null && (stack == null || stack.isEmpty()));
      }

    };
  }

  public ISong search(ISong data) {
    Node<T> target = searchHelper(root, (T) data);
    return (ISong) target.data;
  }

  private Node<T> searchHelper(Node<T> root, T val) {
    if (root == null)
      throw new NoSuchElementException("no element with such value");
    if (root.data.compareTo(val) == 0)
      return root;
    if (root.data.compareTo(val) > 0)
      return searchHelper(root.leftChild, val);
    return searchHelper(root.rightChild, val);
  }

  public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot remove null references.");
    if (root == null) {
      throw new IllegalArgumentException("This RedBlackTree is null");
    }
    if (!contains(data)) {
      throw new IllegalArgumentException("This RedBlackTree does not contain that value.");
    }

    Node<T> newNode = new Node<>(data);

    if (root.data == newNode.data && (root.leftChild == null && root.rightChild == null)) {
      root = null;
      size--;
      return true;
    } else {

      boolean returnValue = removeHelper(newNode, root);
      if (returnValue)
        size--;
      else
        returnValue = false;
      return returnValue;
    }
  }

  private boolean removeHelper(Node<T> node, Node<T> subtree) {

    int compare = node.data.compareTo(subtree.data);

    if (compare == 0) {
      Node<T> movedUpNode = null;
      int deletedNodeColor = 0;

      if (subtree.leftChild == null || subtree.rightChild == null) {

        movedUpNode = removeForAtleastOneChild(subtree);
        deletedNodeColor = subtree.blackHeight;

      }

      if (subtree.leftChild != null && subtree.rightChild != null) {

        Node<T> successor = least(subtree.rightChild);
        subtree.data = successor.data;

        movedUpNode = removeForAtleastOneChild(successor);
        deletedNodeColor = successor.blackHeight;

      }

      if (deletedNodeColor == 1) {

        enforceRBTPropertiesAfterRemove(movedUpNode);

        if (movedUpNode.data == null) {
          moveChild(movedUpNode.parent, movedUpNode, null);
        }
      }

    } else if (compare < 0) {
      removeHelper(node, subtree.leftChild);
    } else {
      removeHelper(node, subtree.rightChild);
    }
    root.blackHeight = 1;
    return true;

  }

  private void moveChild(Node<T> parent, Node<T> deletedChild, Node<T> newChild) {
    if (parent == null) {
      root = newChild;
    } else if (parent.leftChild == deletedChild) {
      parent.leftChild = newChild;
    } else if (parent.rightChild == deletedChild) {
      parent.rightChild = newChild;
    } else {
      throw new IllegalStateException("Node is not a child of its parent");
    }

    if (newChild != null) {
      newChild.parent = parent;
    }
  }

  private Node<T> least(Node<T> node) {

    while (node.leftChild != null) {
      node = node.leftChild;
    }
    return node;

  }

  private Node<T> removeForAtleastOneChild(Node<T> node) {

    if (node.leftChild != null) {
      moveChild(node.parent, node, node.leftChild);
      return node.leftChild;
    }

    else if (node.rightChild != null) {
      moveChild(node.parent, node, node.rightChild);
      return node.rightChild;
    }

    else {
      Node<T> newChild;
      if (node.blackHeight == 1) {
        newChild = new Node<>(null);
        newChild.blackHeight = 1;
      } else {
        newChild = null;
      }

      moveChild(node.parent, node, newChild);
      return newChild;
    }

  }

  private void enforceRBTPropertiesAfterRemove(Node<T> node) {
    if (node == root) {
      node.blackHeight = 1;
      return;
    }

    Node<T> sibling = getSiblingNode(node);

    if (sibling.blackHeight == 0) {
      fixRedSibling(node, sibling);
      sibling = getSiblingNode(node);
    }

    if ((sibling.leftChild != null && sibling.leftChild.blackHeight == 1)
        && (sibling.leftChild != null && sibling.rightChild.blackHeight == 1)) {
      sibling.blackHeight = 0;
      if (node.parent.blackHeight == 0) {
        node.parent.blackHeight = 1;
      } else {
        enforceRBTPropertiesAfterRemove(node.parent);
      }
    }

    else {
      fixBlackSiblingWithOneOrZeroChild(node, sibling);
    }
  }

  private void fixBlackSiblingWithOneOrZeroChild(Node<T> node, Node<T> siblingNode) {

    if (node.isLeftChild() && siblingNode.rightChild != null
        && siblingNode.rightChild.blackHeight == 1) {

      siblingNode.leftChild.blackHeight = 1;
      siblingNode.blackHeight = 0;
      rightRotation(siblingNode);
      siblingNode = node.parent.rightChild;
    } else if (!node.isLeftChild() && siblingNode.leftChild != null
        && siblingNode.leftChild.blackHeight == 1) {

      siblingNode.rightChild.blackHeight = 1;
      siblingNode.blackHeight = 0;
      rotateLeft(siblingNode);
      siblingNode = node.parent.leftChild;
    }

    siblingNode.blackHeight = node.parent.blackHeight;
    node.parent.blackHeight = 1;
    if (node.isLeftChild()) {
      if (siblingNode.rightChild != null && siblingNode.leftChild == null) {
        siblingNode.rightChild.blackHeight = 1;
        rotateLeft(node.parent);
        return;
      }
      if (siblingNode.leftChild != null && siblingNode.rightChild == null) {
        rightRotation(siblingNode);
        rotateLeft(node.parent);
        siblingNode.blackHeight = 1;
        return;
      }

      if (siblingNode.leftChild != null && siblingNode.rightChild != null) {
        rightRotation(node.parent);
        siblingNode.rightChild.blackHeight = 1;
        return;
      }

    }
    if (!node.isLeftChild()) {
      if (siblingNode.leftChild != null && siblingNode.rightChild == null) {
        siblingNode.leftChild.blackHeight = 1;
        rightRotation(node.parent);
        return;
      }
      if (siblingNode.rightChild != null && siblingNode.leftChild == null) {
        rotateLeft(siblingNode);
        rightRotation(node.parent);
        siblingNode.blackHeight = 1;
        return;
      }

      if (siblingNode.leftChild != null && siblingNode.rightChild != null) {
        rightRotation(node.parent);
        siblingNode.leftChild.blackHeight = 1;
        return;
      }
    }
  }

  private void fixRedSibling(Node<T> node, Node<T> redSibling) {
    // recolor sibling to black and parent of the node to red
    redSibling.blackHeight = 1;
    node.parent.blackHeight = 0;

    // rotate left is node is parent of left chile else, do otherwise
    if (node == node.parent.leftChild) {
      rotateLeft(node.parent);
    } else {
      rightRotation(node.parent);
    }
  }

  private void rotateLeft(Node<T> node) {
    Node<T> parent = node.parent;
    Node<T> child = node.rightChild;

    node.rightChild = child.leftChild;
    if (child.leftChild != null) {
      child.leftChild.parent = node;
    }

    child.leftChild = node;
    node.parent = child;

    moveChild(parent, node, child);
  }

  private void rightRotation(Node<T> node) {
    Node<T> parent = node.parent;
    Node<T> child = node.leftChild;

    node.leftChild = child.rightChild;
    if (child.rightChild != null) {
      child.rightChild.parent = node;
    }

    child.rightChild = node;
    node.parent = child;

    moveChild(parent, node, child);
  }

  private Node<T> getSiblingNode(Node<T> node) {
    Node<T> parent = node.parent;
    if (node == parent.leftChild) {
      return parent.rightChild;
    } else if (node == parent.rightChild) {
      return parent.leftChild;
    } else {
      throw new IllegalStateException("can't get sibling, invalid relation");
    }
  }
}