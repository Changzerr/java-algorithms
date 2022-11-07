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

    public T addNode(T value) {
        SearchTreeNode<T> node = new SearchTreeNode<T>(value);
        return addNode(node);
    }

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

            if (cmp > 0) {
                x.setLeft(node);
            } else {
                x.setRight(node);
            }
            size.incrementAndGet();
        }
        return node.getValue();
    }

    private SearchTreeNode<T> findParentNode(SearchTreeNode<T> node) {
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
                return cur;
            }
        }
        return parent;
    }

    public boolean removeNode(T value) {
        SearchTreeNode<T> node = new SearchTreeNode<T>(value);
        return removeNode(node);
    }
    public boolean removeNode(SearchTreeNode<T> node) {
        SearchTreeNode<T> parent = findRemoveParentNode(node);
        if (parent == null) {
            return false;
        }
        if (node.getValue().compareTo(parent.getValue()) == 0){
            parent = root;
        }
        if (parent.getLeft() != null &&
                node.getValue().compareTo(parent.getLeft().getValue()) == 0) {

            node = parent.getLeft();
            if(node.getLeft() != null && node.getRight()==null){
                parent.setLeft(node.getLeft());
            }else if(node.getLeft() == null && node.getRight()!=null){
                parent.setLeft(node.getRight());
            }else if(node.getLeft() != null && node.getRight()!=null){
                parent.setLeft(node.getRight());
                SearchTreeNode<T> left = node.getLeft();
                SearchTreeNode<T> right = node.getRight();
                while(right!=null && right.getLeft() != null){
                    right = right.getLeft();
                }
                right.setLeft(left);
            }else {
                parent.setLeft(null);
            }
        }else{
            node = parent.getRight();
            SearchTreeNode<T> left = node.getLeft();
            SearchTreeNode<T> right = node.getRight();

            if(left != null && right==null){
                parent.setRight(node.getLeft());
            }else if(left == null && right!=null){
                parent.setRight(node.getRight());
            }else if(left != null && right!=null){
                parent.setRight(node.getRight());

                while(right!=null &&  right.getLeft() != null){
                    right = right.getLeft();
                }
                right.setLeft(left);
            }else {
                parent.setRight(null);
            }
        }
        return true;
    }



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
