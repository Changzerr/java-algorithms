package tree;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lingqu
 * @date 2022/11/4
 * @apiNote
 */

class RBTreeNode<T extends Comparable<T>> {
    private T value;
    private RBTreeNode<T> parent;
    private RBTreeNode<T> left;
    private RBTreeNode<T> right;
    private boolean red;


    public RBTreeNode(){}
    public RBTreeNode(T value){this.value=value;}
    public RBTreeNode(T value,boolean isRed){this.value=value;this.red = isRed;}

    public T getValue() {
        return value;
    }
    void setValue(T value) {
        this.value = value;
    }
    RBTreeNode<T> getLeft() {
        return left;
    }
    void setLeft(RBTreeNode<T> left) {
        this.left = left;
    }
    RBTreeNode<T> getRight() {
        return right;
    }
    void setRight(RBTreeNode<T> right) {
        this.right = right;
    }
    RBTreeNode<T> getParent() {
        return parent;
    }
    void setParent(RBTreeNode<T> parent) {
        this.parent = parent;
    }

    boolean isRed() {
        return red;
    }
    boolean isBlack(){
        return !red;
    }
    /**
     * is leaf node
     **/
    boolean isLeaf(){
        return left==null && right==null;
    }

    void setRed(boolean red) {
        this.red = red;
    }

    void makeRed(){
        this.red=true;
    }
    void makeBlack(){
        this.red=false;
    }
    @Override
    public String toString(){
        return value.toString();
    }
}

public class RBTree<T extends Comparable<T>> {
    //virtual head node
    private RBTreeNode<T> root;
    private AtomicLong size = new AtomicLong(0);

    //in overwrite mode,all node's value can not  has same    value
    //in non-overwrite mode,node can have same value, suggest don't use non-overwrite mode.
    private boolean overrideMode = true;

    RBTree(){
        this.root = new RBTreeNode<T>();
    }

    RBTree(boolean overrideMode){
        this();
        this.overrideMode = overrideMode;
    }

    private void setParent(RBTreeNode<T> node,RBTreeNode<T> parent){
        if(node!=null){
            node.setParent(parent);
            if(parent==root){
                node.setParent(null);
            }
        }
    }

    public boolean isOverrideMode(){return overrideMode;}
    public void setOverrideMode(boolean overrideMode) {this.overrideMode = overrideMode;}


    /**
     * number of tree number
     * @return
     */
    public long getSize() {
        return size.get();
    }

    /**
     * get the root node
     * @return
     */
    public RBTreeNode<T> getRoot(){
        return root.getLeft();
    }

    /**
     * add value to a new node,if this value exist in this tree,
     * if value exist,it will return the exist value.otherwise return null
     * if override mode is true,if value exist in the tree,
     * it will override the old value in the tree
     *
     * @param value
     * @return
     */
    public T addNode(T value){
        RBTreeNode<T> t = new RBTreeNode<T>(value);
        return addNode(t);
    }

    /**
     * add node
     * @param node
     * @return
     */
    public T addNode(RBTreeNode<T> node){
        //init node
        node.setLeft(null);
        setParent(node,null);
        node.setRight(null);
        node.setRed(true);

        //Determine whether the header node is empty
        if (root.getLeft() == null){
            //node become root
            root.setLeft(node);
            //root is black
            node.makeBlack();
        }else {
            //find insert point
            RBTreeNode<T> x = findParentNode(node);
            int cmp = x.getValue().compareTo(node.getValue());

            //value exists,ignore this node
            if(this.overrideMode && cmp==0){
                T v = x.getValue();
                x.setValue(node.getValue());
                return v;
            }else if(cmp==0){
                return x.getValue();
            }

            //x become node's parent
            setParent(node, x);

            if(cmp>0){
                x.setLeft(node);
            }else{
                x.setRight(node);
            }

            //Keep RBTree's identity.
            fixInsert(node);
        }
        size.incrementAndGet();
        return null;
    }

    /**
     * red black tree insert fix.
     * when Red is connected to red, we need fix RBTress。
     * if insert node's uncle is null(black),
     *          case 1: LL type, rotate right, change the color of parent and ancestor
     *          case 2: RR type, rotate left, change the color of parent and ancestor
     *          case 3: LR type, first rotate left and then rotate right, change the color of current and ancestor
     *          case 4: RL type, first rotate Right and then rotate left, change the color of current and ancestor
     * else insert node's uncle is red:
     *          change the color of parent、uncle with ancestor，and look on the ancestor as new insert node
     * @param node is new node
     */
    private void fixInsert(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();

        //whether need to fix
        while (parent != null && parent.isRed()) {
            //get uncle
            RBTreeNode<T> uncle = getUncle(node);

            if(uncle == null || uncle.isBlack()) {
                //uncle is black, have 4 cases
                RBTreeNode<T> ancestor = parent.getParent();
                if (ancestor.getLeft() == parent){
                    boolean isRight = node == parent.getRight();
                    if (isRight) {
                        rotateLeft(parent);
                    }
                    rotateRight(ancestor);

                    if (isRight) {
                        //change the color of current and ancestor
                        node.makeBlack();

                        //end loop
                        parent = null;
                    }else{
                        //end loop
                        parent.makeBlack();
                    }

                    ancestor.makeRed();
                }else{
                    boolean isLeft = node == parent.getLeft();
                    if (isLeft) {
                        rotateRight(parent);
                    }
                    rotateLeft(ancestor);

                    if (isLeft) {
                        node.makeBlack();

                        //end loop
                        parent = null;
                    }else {
                        //end loop
                        parent.makeBlack();
                    }

                    ancestor.makeRed();
                }
            }else{
                //change the color of parent、uncle with ancestor
                parent.makeBlack();
                uncle.makeBlack();
                RBTreeNode<T> ancestor = parent.getParent();
                ancestor.makeRed();

                //ancestor as new insert node
                node = parent.getParent();
                parent = node.getParent();
            }
        }

        //make sure root is black
        RBTreeNode<T> root = getRoot();
        root.makeBlack();
        setParent(root, null);
    }

    /**
     * rotate right
     * @param node
     */
    private void rotateRight(RBTreeNode<T> node) {
        //get left
        RBTreeNode<T> left = node.getLeft();
        if(left==null){
            throw new java.lang.IllegalStateException("left node is null");
        }
        //get parent
        RBTreeNode<T> parent = node.getParent();

        //leftRight move to node's left
        RBTreeNode<T> leftRight = left.getRight();
        node.setLeft(leftRight);
        setParent(leftRight, node);

        //node move to leftRight
        left.setRight(node);
        setParent(node, left);

        //change left's parent
        if(parent == null) {
            root.setLeft(left);
        }else {
            if (parent.getLeft() == node){
                parent.setLeft(left);
            }else {
                parent.setRight(left);
            }
        }
        setParent(left, parent);
    }

    /**
     * rotate left
     * @param node
     */
    private void rotateLeft(RBTreeNode<T> node) {
        //get right
        RBTreeNode<T> right = node.getRight();
        if(right==null){
            throw new java.lang.IllegalStateException("right node is null");
        }

        //get parent
        RBTreeNode<T> parent = node.getParent();

        //rightLeft move to node's right
        RBTreeNode<T> rightLeft = right.getLeft();
        node.setRight(rightLeft);
        setParent(rightLeft, node);

        //node move to rightLeft
        right.setLeft(node);
        setParent(node, right);

        //change left's parent
        if (parent == null){
            root.setLeft(right);
        }else{
            if (parent.getLeft() == node){
                parent.setLeft(right);
            }else{
                parent.setRight(right);
            }
        }
        setParent(right, parent);
    }


    /**
     * get uncle node
     * @param node
     * @return
     */
    private RBTreeNode<T> getUncle(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();
        RBTreeNode<T> ancestor = parent.getParent();

        if (ancestor == null){
            return null;
        }

        if (parent == ancestor.getLeft()){
            return ancestor.getRight();
        }else {
            return ancestor.getLeft();
        }
    }

    /**
     * find the parent node to hold node ,if parent value equals node.value return parent.
     * be used to find insert position
     * @param node
     * @return
     */
    private RBTreeNode<T> findParentNode(RBTreeNode<T> node) {
        //get root node
        RBTreeNode<T> parent = getRoot();
        RBTreeNode<T> cur = parent;
        while (cur != null){
            int cmp = cur.getValue().compareTo(node.getValue());
            if (cmp == 0){
                //the same value, return it
                return cur;
            }else if (cmp < 0){
                //Greater than, right
                parent = cur;
                cur = cur.getRight();
            }else {
                //Less than, left
                parent = cur;
                cur = cur.getLeft();
            }
        }

        return parent;
    }



    /**
     * debug method,it used print the given node and its children nodes,
     * every layer output in one line
     * @param root
     */
    public void printTree(RBTreeNode<T> root){
        java.util.LinkedList<RBTreeNode<T>> queue =new java.util.LinkedList<RBTreeNode<T>>();
        java.util.LinkedList<RBTreeNode<T>> queue2 =new java.util.LinkedList<RBTreeNode<T>>();
        if(root==null){
            return ;
        }
        queue.add(root);
        boolean firstQueue = true;

        while(!queue.isEmpty() || !queue2.isEmpty()){
            java.util.LinkedList<RBTreeNode<T>> q = firstQueue ? queue : queue2;
            RBTreeNode<T> n = q.poll();

            if(n!=null){
                String pos = n.getParent()==null ? "" : ( n == n.getParent().getLeft()
                        ? " LE" : " RI");
                String pstr = n.getParent()==null ? "" : n.getParent().toString();
                String cstr = n.isRed()?"R":"B";
                cstr = n.getParent()==null ? cstr : cstr+" ";
                System.out.print(n+"("+(cstr)+pstr+(pos)+")"+"\t");
                if(n.getLeft()!=null){
                    (firstQueue ? queue2 : queue).add(n.getLeft());
                }
                if(n.getRight()!=null){
                    (firstQueue ? queue2 : queue).add(n.getRight());
                }
            }else{
                System.out.println();
                firstQueue = !firstQueue;
            }
        }
    }
}
