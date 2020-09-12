package semisplay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Node of a certain type @param <E>
 */
public class Node<E extends Comparable<E>> {

    private E value;
    private Node<E> lChild;
    private Node<E> rChild;
    private Node<E> parent;

    public Node(E c, Node<E> parent) {
        value = c;
        lChild = null;
        rChild = null;
        this.parent = parent;
    }

    /**
     * Adds a value to the currect partial tree
     *
     * @param v value to be added
     * @param path from root to added child
     * @return if added
     * */

    public boolean addChild(E v, ArrayList<Node<E>> path) {
        boolean done = false;
        path.add(this);
        Node<E> child = getApropChild(v);
        if (child != null) {
            done = child.addChild(v, path);
        } else {
            if (v.compareTo(value) < 0){
                lChild = new Node<E>(v, this);
                path.add(lChild);
                done = true;
            } else if (v.compareTo(value) > 0){
                rChild = new Node<E>(v, this);
                done = true;
                path.add(rChild);
            }
        }
        return done;
    }

    /**
     * Checks if the current (partial) tree contains a certain value
     *
     * @param v value to be checked
     * @param path to the root node
     */
    public boolean contains(E v, ArrayList<Node<E>> path) {
        path.add(this);
        boolean done = false;
        Node<E> child = getApropChild(v);
        if (child != null) {
            done = child.contains(v, path);
        } else if (v.compareTo(value) == 0){
            done = true;
        }
        return done;
    }

    /**
     * Remove the current node from the tree if the value equals the value of the current node
     * and links the children
     * Calls remove method on correct child if current node doesn't get removed
     *
     * @param v the value to be removed
     * @return if the value is removed
     */
    public boolean remove(E v, ArrayList<Node<E>> path) {
        Node<E> child = getApropChild(v);
        boolean done = false;
        if (child != null) {
            path.add(this);
            return child.remove(v, path);
        } else if (v.compareTo(value) == 0){
            if (lChild == null && rChild == null) {
                parent.setChild(value, null);
            } else if (lChild == null != (rChild == null)) {
                if (lChild == null) {
                    parent.setChild(value, rChild);
                    path.add(rChild);
                } else {
                    parent.setChild(value, lChild);
                    path.add(lChild);
                }
            } else {
                Node<E> replace = lChild.getBiggest();
                parent.setChild(value, replace);
                replace.setChild(value, rChild);
                if (replace != lChild) {
                    replace.setChild(replace.getValue(), lChild);
                }
                path.add(replace);
            }
            done = true;
        }
        return done;
    }

    /**
     * @return the correct child depending on a value @param v
     *
     */
    private Node<E> getApropChild(E v) {
        if (v.compareTo(value) < 0) {
            return lChild;
        } else if (v.compareTo(value) > 0) {
            return rChild;
        }
        return null;
    }

    /**
     * @return the biggest key of the current (partial) tree
     */
    public Node<E> getBiggest() {
        if (rChild != null) {
            return rChild.getBiggest();
        }
        parent.setChild(value, lChild);
        return this;
    }

    /**
     * @return the depth of the current (partial) tree
     */
    public int depth() {
        int ldepth = 0;
        int rdepth = 0;
        if (lChild != null) {
            ldepth = lChild.depth();
        }
        if (rChild != null) {
            rdepth = rChild.depth();
        }
        return Math.max(ldepth, rdepth) + 1;
    }

    /**
     * @return the amount of keys in the current (partial)tree
     */
    public int size() {
        int lsize = 0;
        int rsize = 0;
        if (lChild != null) {
            lsize = lChild.size();
        }
        if (rChild != null) {
            rsize = rChild.size();
        }
        return lsize + rsize + 1;
    }

    /**
     * @return the left child of the current node
     */
    public Node<E> getLChild() {
        return lChild;
    }

    /**
     * @return the right child of the current node
     */
    public Node<E> getRChild() {
        return rChild;
    }

    /**
     * Zet een bepaalde top @param c op een bepaald kind van de huidige top
     * Als de waarde @param v groter is dan de waarde van de huidige top zet @param c als rechterkind
     * is deze kleiner, zet deze dan op het linkerkind.
     * Roep #setParent() op indien nodig
     */
    public void setChild(E v, Node<E> c) {
        if (v.compareTo(value) <= 0) {
            lChild = c;
        } else if (v.compareTo(value) > 0) {
            rChild = c;
        }
        if (c != null) {
            c.setParent(this);
        }
    }


    /**
     * Set the parent of the current node
     *
     * @param p is the parent
     */
    public void setParent(Node<E> p) {
        parent = p;
    }

    /**
     * @return the value of this node
     */
    public E getValue() {
        return value;
    }

    /**
     * @return lijst van alle keys in gesorteerde volgorde
     */
    public ArrayList<E> getAllKeys() {
        ArrayList<E> list;
        if (lChild != null) {
            list = lChild.getAllKeys();
        } else {
            list = new ArrayList<>();
        }
        list.add(value);
        if (rChild != null) {
            list.addAll(rChild.getAllKeys());
        }
        return list;
    }

    public class DT {
        public ArrayList<String> lines;
        public int width;
        public int height;
        public int rootCoor;

        private DT(ArrayList value, int width, int height, int middle) {
            this.lines = value;
            this.width = width; // n
            this.height = height; // p
            this.rootCoor = middle; // x

        }
    }

    private String rChar(char c, int length) {
        char[] data = new char[length];
        Arrays.fill(data, c);
        return new String(data);
    }

    private String[] fillArray(String value, int length) {
        String[] data = new String[length];
        Arrays.fill(data, value);
        return data;
    }

    public DT printNode() {

        String valueString = value.toString(); // s
        int lenValue = valueString.length(); // = u
        int middleValue = Math.floorDiv(lenValue, 2); // = (u // 2)

        //no children
        if (rChild == null && lChild == null) {
            ArrayList lines = new ArrayList();
            lines.add(valueString);
            return new DT(lines, lenValue, 1, middleValue);
        }

        //left child
        else if (rChild == null) {
            DT leftTree = lChild.printNode();
            String firstLine = rChar(' ', leftTree.rootCoor + 1) + rChar('_', leftTree.width - leftTree.rootCoor - 1) + valueString; // vb:"      ________23"
            String secLine = rChar(' ', leftTree.rootCoor) + "/" + rChar(' ', leftTree.width - leftTree.rootCoor - 1 + lenValue); // vb:"       /        "
            ArrayList shiftedLines = new ArrayList<>(leftTree.lines.stream().map(l -> l + rChar(' ', lenValue)).collect(Collectors.toList()));
            List<String> newLines = Arrays.asList(firstLine, secLine);
            shiftedLines.addAll(0, newLines);
            return new DT(shiftedLines, leftTree.width + lenValue, leftTree.height + 2, leftTree.width + middleValue);
        }

        //right child
        else if (lChild == null) {
            DT rightTree = rChild.printNode();
            String firstLine = valueString + rChar('_', rightTree.rootCoor) + rChar(' ', rightTree.width - rightTree.rootCoor);
            String secLine = rChar(' ', lenValue + rightTree.rootCoor) + "\\" + rChar(' ', rightTree.width - rightTree.rootCoor - 1);
            ArrayList shiftedLines = new ArrayList<>(rightTree.lines.stream().map(l -> rChar(' ', lenValue) + l).collect(Collectors.toList()));
            List<String> newLines = Arrays.asList(firstLine, secLine);
            shiftedLines.addAll(0, newLines);
            return new DT(shiftedLines, rightTree.width + lenValue, rightTree.height + 2, middleValue);
        }

        //two children
        else {
            DT leftTree = lChild.printNode();
            DT rightTree = rChild.printNode();
            String firstLine = rChar(' ', leftTree.rootCoor + 1) + rChar('_', leftTree.width - leftTree.rootCoor - 1) + valueString + rChar('_', rightTree.rootCoor) + rChar(' ', rightTree.width - rightTree.rootCoor);
            String secLine = rChar(' ', leftTree.rootCoor) + "/" + rChar(' ', leftTree.width - leftTree.rootCoor - 1 + lenValue + rightTree.rootCoor) + "\\" + rChar(' ', rightTree.width - rightTree.rootCoor - 1);
            ArrayList<String> lt = new ArrayList<>(leftTree.lines);
            ArrayList<String> rt = new ArrayList<>(rightTree.lines);
            if (leftTree.height < rightTree.height) {
                lt.addAll(Stream.of(fillArray(rChar(' ', leftTree.width), (rightTree.height - leftTree.height))).collect(Collectors.toList()));
            } else if (leftTree.height > rightTree.height) {
                rt.addAll(Stream.of(fillArray(rChar(' ', rightTree.width), (leftTree.height - rightTree.height))).collect(Collectors.toList()));
            }
            ArrayList<String> zippedLines = new ArrayList<>(IntStream.range(0, rt.size()).mapToObj(i -> lt.get(i) + rChar(' ', lenValue) + rt.get(i)).collect(Collectors.toList()));
            List<String> newLines = Arrays.asList(firstLine, secLine);
            zippedLines.addAll(0, newLines);
            return new DT(zippedLines, leftTree.width + rightTree.width + lenValue, Math.max(leftTree.height, rightTree.height) + 2, leftTree.width + middleValue);
        }

    }
}
