package tree;

import org.junit.jupiter.api.Test;

/**
 * @author lingqu
 * @date 2022/11/5
 * @apiNote
 */
public class RBTreeTest {
    @Test
    public void testTree() {
        RBTree<Integer> bst = new RBTree<Integer>();
        bst.addNode(20);
        bst.addNode(10);
        bst.addNode(5);
        bst.addNode(30);
        bst.addNode(40);
        bst.addNode(57);

        bst.addNode(3);
        bst.addNode(2);

        bst.addNode(4);
        bst.addNode(35);
        bst.addNode(25);
        bst.addNode(18);
        bst.addNode(22);
        bst.addNode(23);
        bst.addNode(24);
        bst.addNode(19);
        bst.addNode(18);


        //bst.remove(2);

        bst.printTree(bst.getRoot());
    }
}
