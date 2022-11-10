package tree;

import org.junit.jupiter.api.Test;

/**
 * @author changzer
 * @date 2022/11/10
 * @apiNote
 */
public class AVLTreeTest {

    @Test
    public  void testAVLInsert() {
        AVLTree aTree = new AVLTree();
        int[] arr = { 3, 2, 1, 4, 5, 6, 7, 10, 9, 8 };
        for (int i : arr) {
            aTree.insert(i);
        }
        System.out.print("前序遍历结果：");
        aTree.preOrder();
        System.out.print("中序遍历结果：");
        aTree.inOrder();

        AVLTree bTree = new AVLTree();
        int[] arr2 = { 3,2,1,4,5,6,7,16,15,14,13,12,11,10,8,9 };
        for (int i : arr2) {
            bTree.insert(i);
        }
        System.out.print("前序遍历结果：");
        bTree.preOrder();
        System.out.print("中序遍历结果：");
        bTree.inOrder();
    }

}
