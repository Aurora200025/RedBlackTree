package RedBlackTree;

import java.util.Comparator;

public class RBTree<E> extends BalancedBinarySearchTree<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree() {
        this(null);
    }
    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    @Override
    protected void afterAdd(BinaryTree.Node<E> node) {
        BinaryTree.Node<E> parent = node.parent;
        if (parent == null) {//如果是添加的根节点，则直接染成黑色后返回
            black(node);
            return;
        }
        if (isBlack(parent)) {//如果父节点是黑色直接返回
            return;
        }
        //判断叔父节点-uncle
        BinaryTree.Node<E> uncle = parent.sibling();
        BinaryTree.Node<E> grand = parent.parent;
        if (isRed(uncle)) { //uncle是红色的话
            black(parent);
            black(uncle);
            afterAdd(red(grand));   //相当于上溢，就把grand染红当成新节点
            return;
        }
        //叔父节点不是红色
        if (parent.isLeftChild()) {//L
            red(grand);
            if (node.isLeftChild()) { //LL
                black(parent);
//                red(grand);
//                rotateRight(grand);
            }else { //LR
                black(node);
//                red(grand);
                rotateLeft(parent);
//                rotateRight(grand);
            }
            rotateRight(grand);
        }else { //R
            red(grand);
            if (node.isLeftChild()) { //RL
                black(node);
//                red(grand);
                rotateRight(parent);
//                rotateLeft(grand);
            }else { //RR
                black(parent);
//                red(grand);
//                rotateLeft(grand);
            }
            rotateLeft(grand);
        }
    }
    @Override
    protected void afterRemove(BinaryTree.Node<E> node) {
//        if (isRed(node)) {//如果删除节点为红色，不用处理
//            return;
//        }
        //如果删除节点为红色 或者用来取代node的子节点为红色
        if (isRed(node)) {
            black(node);
            return;
        }
        BinaryTree.Node<E> parent = node.parent;
        //则删除为黑色叶子节点--根节点
        if (parent == null) {
            return;
        }
        //不是根节点--黑色
        boolean isLeftNull = parent.left == null || node.isLeftChild();
        BinaryTree.Node<E> sibling = isLeftNull ? parent.right : parent.left;
        if (isLeftNull) { //被删除节点在左边，兄弟节点在右边
            if (isRed(sibling)) {   //兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                sibling = parent.right;
            }
            // 来到这里，兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                //兄弟节点没有一个红色子节点-不可以借东西给你
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            }else {//至少一个红色子节点--可以借元素
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        }else { //被删除节点在右边，兄弟节点在左边
            if (isRed(sibling)) {   //兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                sibling = parent.left;
            }
            // 来到这里，兄弟节点为黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                //兄弟节点没有一个红色子节点-不可以借东西给你
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            }else {//至少一个红色子节点--可以借元素
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }
    //以下注释为存在replacement节点和node节点的情况下
//    @Override
//    protected void afterRemove(Node<E> node, Node<E> replacement) {
//        if (isRed(node)) {//如果删除节点为红色，不用处理
//            return;
//        }
//        if (isRed(replacement)) {   //用户取代node的子节点为红色
//            black(replacement);
//            return;
//        }
//        Node<E> parent = node.parent;
//        //则删除为黑色叶子节点--根节点
//        if (parent == null) {
//            return;
//        }
//        //不是根节点--黑色
//        boolean isLeftNull = parent.left == null || node.isLeftChild();
//        Node<E> sibling = isLeftNull ? parent.right : parent.left;
//        if (isLeftNull) { //被删除节点在左边，兄弟节点在右边
//            if (isRed(sibling)) {   //兄弟节点是红色
//                black(sibling);
//                red(parent);
//                rotateLeft(parent);
//                sibling = parent.right;
//            }
//            // 来到这里，兄弟节点为黑色
//            if (isBlack(sibling.left) && isBlack(sibling.right)) {
//                //兄弟节点没有一个红色子节点-不可以借东西给你
//                boolean parentBlack = isBlack(parent);
//                black(parent);
//                red(sibling);
//                if (parentBlack) {
//                    afterRemove(parent, null);
//                }
//            }else {//至少一个红色子节点--可以借元素
//                if (isBlack(sibling.right)) {
//                    rotateRight(sibling);
//                    sibling = parent.right;
//                }
//                color(sibling, colorOf(parent));
//                black(sibling.right);
//                black(parent);
//                rotateLeft(parent);
//            }
//        }else { //被删除节点在右边，兄弟节点在左边
//            if (isRed(sibling)) {   //兄弟节点是红色
//                black(sibling);
//                red(parent);
//                rotateRight(parent);
//                sibling = parent.left;
//            }
//            // 来到这里，兄弟节点为黑色
//            if (isBlack(sibling.left) && isBlack(sibling.right)) {
//                //兄弟节点没有一个红色子节点-不可以借东西给你
//                boolean parentBlack = isBlack(parent);
//                black(parent);
//                red(sibling);
//                if (parentBlack) {
//                    afterRemove(parent, null);
//                }
//            }else {//至少一个红色子节点--可以借元素
//                if (isBlack(sibling.left)) {
//                    rotateLeft(sibling);
//                    sibling = parent.left;
//                }
//                color(sibling, colorOf(parent));
//                black(sibling.left);
//                black(parent);
//                rotateRight(parent);
//            }
//        }
//    }

    //-----------------------------------------------------
    //-----------------------------------------------------
    /**
     * @param node 为该节点染色
     */
    private BinaryTree.Node<E> color(BinaryTree.Node<E> node, boolean color) {
        if (node == null) {
            return null;
        }
        ((RBNode<E>) node).color = color;
        return node;
    }

    /**
     * @param node 将该节点染成红色
     */
    private BinaryTree.Node<E> red(BinaryTree.Node<E> node) {
        return color(node, RED);
    }

    /**
     * @param node 将该节点染成黑色
     */
    private BinaryTree.Node<E> black(BinaryTree.Node<E> node) {
        return color(node, BLACK);
    }

    /**
     * @param node 在红黑树中如果是null节点则为黑色，判断是否为null
     */
    private boolean colorOf(BinaryTree.Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    /**
     * @param node 判断颜色是否属于黑色
     */
    private boolean isBlack(BinaryTree.Node<E> node) {
        return colorOf(node) == BLACK;
    }

    /**
     * @param node 判断颜色是否属于红色
     */
    private boolean isRed(BinaryTree.Node<E> node) {
        return colorOf(node) == RED;
    }

    @Override
    protected BinaryTree.Node<E> createNode(E element, BinaryTree.Node<E> parent) {
        return new RBNode<E> (element, parent);
    }

    //===============================================
    private static class RBNode<E> extends BinaryTree.Node<E> {

        boolean color = RED; //规定 true--BLACK,false--RED

        public RBNode(E element, BinaryTree.Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element;
        }
    }
}
