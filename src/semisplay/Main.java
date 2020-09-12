package semisplay;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Main {

    public static void main(String[] args) {
        SemiSplayTree<String> tree = new SemiSplayTree<>(4);
        Timer t = new Timer();
        SemiSplayTree<Integer> test = new SemiSplayTree<>(3);
        test.add(50);
        test.add(5);
        test.add(40);
        test.add(38);
        test.add(20);
        test.add(35);
        test.add(25);
        test.print();
        test.remove(50);
        test.print();
        System.out.println("Lasted: " + t.delta());


    }

    @Test
    public void evaluatesExpression() {
        SemiSplayTree<Integer> semiSplayTree = new SemiSplayTree<>(4);
        semiSplayTree.add(4);
        assertEquals(1, semiSplayTree.depth());
    }

}
