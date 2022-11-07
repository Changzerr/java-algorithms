package tree;

import org.junit.jupiter.api.Test;

/**
 * @author changzer
 * @date 2022/11/7
 * @apiNote
 */
public class SearchTreeTest {
    @Test
    public void testSearchTreeInsert() {
        SearchTree<Integer> tree = new SearchTree<Integer>();
        tree.addNode(3);
        tree.addNode(7);
        tree.addNode(1);
        tree.addNode(33);
        tree.addNode(111);
        tree.printTree(tree.getRoot());
    }
}
