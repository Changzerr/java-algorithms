package tree;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author changzer
 * @date 2022/11/7
 * @apiNote
 */
class SearchTreeNode<T extends Comparable<T>> {
    private T value;
    private SearchTreeNode<T> left;
    private SearchTreeNode<T> right;

    public SearchTreeNode(T value) {
        this.value = value;
    }

    public SearchTreeNode() {
    }

    public T getValue() {
        return value;
    }

    public SearchTreeNode(T value, SearchTreeNode<T> left, SearchTreeNode<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public SearchTreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(SearchTreeNode<T> left) {
        this.left = left;
    }

    public SearchTreeNode<T> getRight() {
        return right;
    }

    public void setRight(SearchTreeNode<T> right) {
        this.right = right;
    }


    @Override
    public String toString() {
        return "SearchTreeNode{" +
                "value=" + value +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

}

public class SearchTree<T extends Comparable<T>> {
    private SearchTreeNode<T> root;
    private AtomicLong size = new AtomicLong(0);

    public SearchTree() {
        this.root = new SearchTreeNode<T>();
    }

    public SearchTreeNode<T> getRoot() {
        return root.getLeft();
    }

    public long getSize() {
        return size.get();
    }

    /**
     * 添加结点
     * @param value
     * @return
     */
    public T addNode(T value) {
        SearchTreeNode<T> node = new SearchTreeNode<T>(value);
        return addNode(node);
    }

    /**
     * 添加结点
     * @param node
     * @return
     */
    public T addNode(SearchTreeNode<T> node) {
        //init node
        node.setLeft(null);
        node.setRight(null);

        //Determine whether the header node is empty
        if (root.getLeft() == null) {
            //node become root
            root.setLeft(node);
            size.incrementAndGet();
        } else {
            //find insert point
            SearchTreeNode<T> x = findParentNode(node);
            int cmp = x.getValue().compareTo(node.getValue());

            //value exists,ignore this node
            if (cmp == 0) {
                return x.getValue();
            }

            //插入位置
            if (cmp > 0) {
                x.setLeft(node);
            } else {
                x.setRight(node);
            }
            size.incrementAndGet();
        }
        return node.getValue();
    }

    /**
     * 找到插入位置
     * @param node
     * @return
     */
    private SearchTreeNode<T> findParentNode(SearchTreeNode<T> node) {
        SearchTreeNode<T> parent = getRoot();
        SearchTreeNode<T> cur = parent;

        while (cur != null) {
            int cmp = cur.getValue().compareTo(node.getValue());
            if (cmp > 0) {
                //当前值大，向左
                parent = cur;
                cur = cur.getLeft();
            } else if (cmp < 0) {
                //当前值大，向右
                parent = cur;
                cur = cur.getRight();
            } else {
                return cur;
            }
        }
        return parent;
    }


    /**
     * 删除结点
     * @param value
     * @return
     */
    public boolean removeNode(T value) {
        //查找所需删除的结点
        SearchTreeNode<T> node = findRemoveNode(value);

        //没有找到, 删除失败
        if (node == null) {
            return false;
        }
        return removeNode(node);
    }

    /**
     * 具体删除操作
     * @param node
     * @return
     */
    public boolean removeNode(SearchTreeNode<T> node) {
        //查找所需删除结点的父结点
        SearchTreeNode<T> parent = findRemoveParentNode(node);

        //判断是否是根结点
        if (node.getValue().compareTo(parent.getValue()) == 0){
            parent = root;
        }

        SearchTreeNode<T> left = node.getLeft();
        SearchTreeNode<T> right = node.getRight();

        //当前结点在父结点的左边
        if (parent.getLeft() != null &&
                node.getValue().compareTo(parent.getLeft().getValue()) == 0) {
            if(left != null && right==null){
                //删除结点只有一个孩子，孩子直接顶替该结点
                parent.setLeft(node.getLeft());

            }else if(left == null && right!=null){
                //删除结点只有一个孩子，孩子直接顶替该结点
                parent.setLeft(node.getRight());

            }else if(left!= null && right!=null){
                //结点左右都有孩子，将左孩子挂到右孩子最左孩子的左边
                parent.setLeft(node.getRight());
                leftToRight(right,left);
            }else {
                //为叶子结点，直接删除
                parent.setLeft(null);
            }
        }else{
            if(left != null && right==null){
                //删除结点只有一个孩子，孩子直接顶替该结点
                parent.setRight(node.getLeft());

            }else if(left == null && right!=null){
                //删除结点只有一个孩子，孩子直接顶替该结点
                parent.setRight(node.getRight());

            }else if(left != null && right!=null){
                //结点左右都有孩子，将左孩子挂到右孩子最左孩子的左边
                parent.setRight(node.getRight());
                leftToRight(right,left);

            }else {
                //为叶子结点，直接删除
                parent.setRight(null);
            }
        }
        //数量减一
        size.decrementAndGet();
        return true;
    }


    /**
     * 将左孩子挂到右孩子最左孩子的左边
     * @param right
     * @param left
     */
    private void leftToRight(SearchTreeNode<T> right, SearchTreeNode<T> left) {
        while(right!=null && right.getLeft() != null){
            right = right.getLeft();
        }
        right.setLeft(left);
    }

    /**
     * 查找被删除结点的父结点
     * @param node
     * @return
     */
    private SearchTreeNode<T> findRemoveParentNode(SearchTreeNode<T> node) {
        SearchTreeNode<T> parent = getRoot();
        SearchTreeNode<T> cur = parent;
        while (cur != null) {
            int cmp = cur.getValue().compareTo(node.getValue());
            if (cmp > 0) {
                parent = cur;
                cur = cur.getLeft();
            } else if (cmp < 0) {
                parent = cur;
                cur = cur.getRight();
            } else {
                return parent;
            }
        }
        return null;
    }

    /**
     * 查找是否是存在需要删除的结点
     * @param node
     * @return
     */
    private SearchTreeNode<T> findRemoveNode(T node) {
        SearchTreeNode<T> cur = getRoot();
        while (cur != null) {
            int cmp = cur.getValue().compareTo(node);
            if (cmp > 0) {
                cur = cur.getLeft();
            } else if (cmp < 0) {
                cur = cur.getRight();
            } else {
                return cur;
            }
        }
        return null;
    }

    /**
     * debug method,it used print the given node and its children nodes,
     * every layer output in one line
     *
     * @param root
     */
    public void printTree(SearchTreeNode<T> root) {
        LinkedList<SearchTreeNode<T>> queue = new LinkedList<SearchTreeNode<T>>();
        if (root == null) {
            return;
        }
        queue.add(root);

        while (!queue.isEmpty()) {
            LinkedList<SearchTreeNode<T>> queue2 = new LinkedList<SearchTreeNode<T>>();
            while (!queue.isEmpty()) {
                SearchTreeNode<T> n = queue.poll();
                System.out.print(n.getValue().toString() + "\t");
                if (n.getLeft() != null) {
                    queue2.add(n.getLeft());
                }
                if (n.getRight() != null) {
                    queue2.add(n.getRight());
                }
            }
            queue = queue2;
            System.out.println();
        }
    }
}
