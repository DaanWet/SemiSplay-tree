package semisplay;

import java.util.*;

import static java.lang.Math.ceil;

/**
 * Semi splay tree of a certain type @param <E>
 */
public class SemiSplayTree<E extends Comparable<E>> implements SearchTree<E> {

    private int splaygrootte;
    private Node<E> root;


    public SemiSplayTree(int k) {
        splaygrootte = k;
        root = null;
    }

    /**
     * Add a value to the tree
     *
     * @return if value is added
     */
    @Override
    public boolean add(E comparable) {
        if (root == null) {
            root = new Node<>(comparable, null);
            return true;
        } else {
            ArrayList<Node<E>> path = new ArrayList<>();
            boolean done = root.addChild(comparable, path);
            splay(path);
            return done;
        }
    }

    /**
     * Checks if value is in tree
     *
     * @return if tree contains value
     */
    @Override
    public boolean contains(E comparable) {
        ArrayList<Node<E>> path = new ArrayList<>();
        boolean done = false;
        if (root != null) {
            done = root.contains(comparable, path);
        }
        splay(path);
        return done;
    }


    /**
     * Remove a given value from the tree
     *
     *
     * @return true if tree contains value and if value is removed
     */
    @Override
    public boolean remove(E comparable) {
        boolean done = false;
        ArrayList<Node<E>> path = new ArrayList<>();
        if (root.getValue() == comparable) {
            Node<E> lChild = root.getLChild();
            Node<E> rChild = root.getRChild();
            if (lChild == null && rChild == null) {
                root = null;
            } else if (lChild == null != (rChild == null)) {
                if (lChild != null){
                    root = lChild;
                } else {
                    root = rChild;
                }
            } else {
                Node<E> replace = lChild.getBiggest();
                root = replace;
                replace.setChild(rChild.getValue(), rChild);
                if (replace != lChild) {
                    replace.setChild(replace.getValue(), lChild);
                }
            }
            done = true;
        } else {
            done = root.remove(comparable, path);
            splay(path);
        }
        return done;
    }

    private void splay(ArrayList<Node<E>> path) {
        Collections.reverse(path);
        int index = 0;
        while (index + splaygrootte <= path.size()) {
            print();
            System.out.println("Index: " + index);
            splayHulp(path, index);
            index += splaygrootte - 1;
        }
    }

    private void splayHulp(ArrayList<Node<E>> path, int index) {
        ArrayList<Node<E>> splaypath = new ArrayList<>(path.subList(index, index + splaygrootte));
        ArrayList<Node<E>> buitenbomen = new ArrayList<>();
        splaypath = createBalanced(splaypath);
        for (Node<E> node : splaypath) {
            if (!splaypath.contains(node.getLChild())) {
                buitenbomen.add(node.getLChild());
            }
            if (!splaypath.contains(node.getRChild())) {
                buitenbomen.add(node.getRChild());
            }
        }
        printList(path, "Full");
        Node<E> top = formTree(splaypath, 0, splaypath.size() - 1, index + splaygrootte < path.size() ? path.get(index + splaygrootte) : null);
        path.set(index + splaygrootte - 1, top);
        int i = 0;
        for (Node<E> node : splaypath) {
            if (node.getLChild() == null && i < buitenbomen.size()) {
                if (buitenbomen.get(i) != null) {
                    node.setChild(buitenbomen.get(i).getValue(), buitenbomen.get(i));
                }
                i++;
            }

            if (node.getRChild() == null && i < buitenbomen.size()) {
                if (buitenbomen.get(i) != null) {
                    node.setChild(buitenbomen.get(i).getValue(), buitenbomen.get(i));
                }
                i++;
            }
        }

    }

    private void printList(List<Node<E>> path, String str) {
        System.out.print(str + "path: [");
        for (Node<E> node : path) {
            System.out.print(node.getValue() + ", ");
        }
        System.out.println("]");
    }

    private ArrayList<Node<E>> createBalanced(ArrayList<Node<E>> path) {
        ArrayList<Node<E>> balanced = new ArrayList<>();
        balanced.add(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            if (path.get(i).getValue().compareTo(path.get(i - 1).getValue()) < 0) {
                balanced.add(0, path.get(i));
            } else {
                balanced.add(path.get(i));
            }
        }
        printList(balanced, "Balenced");
        return balanced;
    }

    public Node<E> formTree(ArrayList<Node<E>> nodes, int start, int end, Node<E> parent) {
        int middle;
        if (start != end) {
            middle = ((end - start + 1) / 2);
        } else {
            middle = 0;
        }
        System.out.println("Start : " + start + ", end: " + end + ", middle: " + middle);

        Node<E> middleNode = nodes.get(start + middle);
        System.out.println("Node: " + middleNode.getValue());
        if (middleNode.getLChild() != null) {
            middleNode.setChild(middleNode.getLChild().getValue(), null);
        }
        if (middleNode.getRChild() != null) {
            middleNode.setChild(middleNode.getRChild().getValue(), null);
        }
        if (parent != null) {
            parent.setChild(middleNode.getValue(), middleNode);
        } else {
            root = middleNode;
        }
        if (start != end) {
            printList(nodes.subList(start, start + middle), "First part");
            formTree(nodes, start, start + middle - 1, middleNode);
            if (start + 1 != end) {
                printList(nodes.subList(start + middle + 1, end + 1), "Second part");
                formTree(nodes, start + middle + 1, end, middleNode);
            }
        }
        return middleNode;
    }

    /**
     * @return amount of keys in tree
     */
    @Override
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.size();
        }
    }

    /**
     * @return depth of the tree
     */
    @Override
    public int depth() {
        if (root == null) {
            return -1;
        } else {
            return root.depth() - 1;
        }
    }

    /**
     * @return een iterator over all keys in sorted order
     */
    @Override
    public Iterator<E> iterator() {
        if (root == null) {
            return Collections.emptyIterator();
        } else {
            return root.getAllKeys().iterator();
        }
    }


    // Print the tree
    public void print() {
        root.printNode().lines.forEach(System.out::println);
    }
}
