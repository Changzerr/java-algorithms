package tree;

/**
 * AVL树
 * @author changzer
 *
 */
public class AVLTree {

    private AVLnode root;

    private class AVLnode {
        int data; // 结点数据
        int bf; // 平衡因子,左高记为1，右高记为-1，平衡记为0
        AVLnode lChild, rChild; // 左右孩子

        public AVLnode(int data) {
            this.data = data;
            bf = 0;
            lChild = null;
            rChild = null;
        }
    }

    /**
     * 右旋
     * 返回新的根结点
     */
    public AVLnode rRotate(AVLnode p) {
        AVLnode l = p.lChild;
        p.lChild = l.rChild;
        l.rChild = p;
        return l;
    }

    /**
     * 左旋
     * 返回新的根结点
     */
    public AVLnode lRotate(AVLnode p) {
        AVLnode r = p.rChild;
        p.rChild = r.lChild;
        r.lChild = p;
        return r;
    }

    /**
     * 左平衡旋转(左子树高度比右子树高2时(左斜)执行的操作)
     * 返回值为新的根结点
     */
    public AVLnode leftBalance(AVLnode p) {
        AVLnode l = p.lChild;
        switch (l.bf) {
        case 1: // 情況(1)
            p.bf = 0;
            l.bf = 0;
            return rRotate(p);
        case -1:
            AVLnode lr = l.rChild;
            switch (lr.bf) {
            case 1: // 情況(2)
                p.bf = -1;
                l.bf = 0;
                break; // break别漏写了
            case -1: // 情況(3)
                p.bf = 0;
                l.bf = 1;
                break;
            case 0: // 情況(4)
                p.bf = 0;
                l.bf = 0;
                break;
            }
            lr.bf = 0;
            // 设置好平衡因子bf后，先左旋
            p.lChild = lRotate(l);// 不能用l=leftBalance(l);
            // 再右旋
            return rRotate(p);
        case 0: // 这种情况书中没有考虑到，情况(5)
            l.bf = -1;
            p.bf = 1;
            return rRotate(p);
        }
        // 以下情况应该是不会出现的，所有情况都已经包括，除非程序还有问题
        System.out.println("bf超出范围，请检查程序！");
        return p;
    }

    /**
     * 右平衡旋转(右子树高度比左子树高2时执行的操作)
     * 返回值为新的根结点
     */
    public AVLnode rightBalance(AVLnode p) {
        AVLnode r = p.rChild;
        switch (r.bf) {
        case -1:
            p.bf = 0;
            r.bf = 0;
            return lRotate(p);
        case 1:
            AVLnode rl = r.lChild;
            switch (rl.bf) {
            case 1:
                r.bf = -1;
                p.bf = 0;
                break;
            case -1:
                r.bf = 0;
                p.bf = 1;
                break;
            case 0:
                r.bf = 0;
                p.bf = 0;
                break;
            }
            rl.bf = 0;
            p.rChild = rRotate(r);
            return lRotate(p);
        case 0:
            p.bf = -1;
            r.bf = 1;
            return lRotate(p);
        }
        // 以下情况应该是不会出现的，所有情况都已经包括，除非程序还有问题
        System.out.println("bf超出范围，请检查程序！");
        return p;
    }

    /**
     * 插入操作
     * 要多定义一个taller变量
     */
    boolean taller;// 树是否长高

    public void insert(int key) {
        root = insert(root, key);
    }

    private AVLnode insert(AVLnode tree, int key) {// 二叉查找树的插入操作一样，但多了树是否长高的判断（树没长高就完全类似BST二叉树），要记得每次对taller赋值
        if (tree == null) {
            taller = true;
            return new AVLnode(key);
        }
        if (key == tree.data) {
            System.out.println("数据重复，无法插入！");
            taller = false;
            return tree;
        } else if (key < tree.data) {
            tree.lChild = insert(tree.lChild, key);
            if (taller == true) { // 左子树长高了，要对tree的平衡度分析
                switch (tree.bf) {
                case 1: // 原本左子树比右子树高，需要左平衡处理
                    taller = false; // 左平衡处理，高度没有增加
                    return leftBalance(tree);
                case 0: // 原本左右子树等高，现因左子树增高而增高
                    tree.bf = 1;
                    taller = true;
                    return tree;
                case -1: // 原本右子树比左子树高，现左右子树相等
                    tree.bf = 0;
                    taller = false;
                    return tree;
                }
            }
        } else if (key > tree.data) {
            tree.rChild = insert(tree.rChild, key);
            if (taller == true) { // 右子树长高了，要对tree的平衡度分析
                switch (tree.bf) {
                case 1: // 原本左子树高，现等高
                    tree.bf = 0;
                    taller = false;
                    return tree;
                case 0: // 原本等高，现右边增高了
                    tree.bf = -1;
                    taller = true;
                    return tree;
                case -1: // 原本右子树高，需右平衡处理
                    taller = false;
                    return rightBalance(tree);
                }
            }
        }
        return tree;
    }

    /**
     * 前序遍历
     */
    public void preOrder() {
        preOrderTraverse(root);
        System.out.println();
    }

    private void preOrderTraverse(AVLnode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.data+" ");
        preOrderTraverse(node.lChild);
        preOrderTraverse(node.rChild);
    }

    /**
     * 中序遍历
     */
    public void inOrder() {
        inOrderTraverse(root);
        System.out.println();
    }

    private void inOrderTraverse(AVLnode node) {
        if (node == null) {
            return;
        }
        inOrderTraverse(node.lChild);
        System.out.print(node.data+" ");
        inOrderTraverse(node.rChild);
    }

}
