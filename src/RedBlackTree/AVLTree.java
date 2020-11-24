package RedBlackTree;

import java.util.Comparator;

public class AVLTree<E> extends BalancedBinarySearchTree<E> {



    public AVLTree() {
        this(null);
    }
    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 恢复平衡的逻辑 -- node是新添加的节点
     * @param node 添加node之后的调整--AVLTree
     */
    @Override
    protected void afterAdd(BinaryTree.Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {//node是否平衡
                //如果平衡就更新高度，否则恢复平衡
                updateHeight(node);
            } else {//恢复平衡
                rebalance2(node);
                break;
            }
        }
    }

    @Override
    protected void afterRemove(BinaryTree.Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                updateHeight(node);
            }else {
                rebalance2(node);
            }
        }
    }

    /**
     * @param node 判断该节点是否平衡
     * @return true--平衡；false -- 不平衡
     */
    private boolean isBalanced(BinaryTree.Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    /**
     * @param node 更新节点高度
     */
    private void updateHeight(BinaryTree.Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }
    @Override
    protected BinaryTree.Node<E> createNode(E element, BinaryTree.Node<E> parent) {
        return new AVLNode<E>(element, parent);
    }

    /**
     * @param grand 恢复平衡--node=高度最低的那个不平衡的点--分开处理
     */
    private void rebalance(BinaryTree.Node<E> grand) {
        BinaryTree.Node<E> parent = (((AVLNode<E>) grand)).tallerChild();
        BinaryTree.Node<E> node = (((AVLNode<E>) parent)).tallerChild();
        if (parent.isLeftChild()) { //L
            if (node.isLeftChild()) { //LL
                //直接左旋转
                rotateRight(grand);
            }else { //LR
                //1.先左旋转
                rotateLeft(parent);
                //2.在右旋转
                rotateRight(grand);
            }
        }else { //R
            if (node.isRightChild()) { //RR
                rotateLeft(grand);
            }else { //RL
                //1.先对parent进行右旋转
                rotateRight(parent);
                //2.在对grant进行左旋转
                rotateLeft(grand);
            }
        }
    }

    /**
     * @param grand 恢复平衡--node=高度最低的那个不平衡的点--统一处理
     */
    private void rebalance2(BinaryTree.Node<E> grand) {
        BinaryTree.Node<E> parent = (((AVLNode<E>) grand)).tallerChild();
        BinaryTree.Node<E> node = (((AVLNode<E>) parent)).tallerChild();
        if (parent.isLeftChild()) { //L
            if (node.isLeftChild()) { //LL
                rotate(grand, node, node.right, parent, parent.right, grand);
            }else { //LR
                rotate(grand, parent, node.left, node, node.right, grand);
            }
        }else { //R
            if (node.isRightChild()) { //RR
                rotate(grand, grand, parent.left, parent, node.left, node);
            }else { //RL
                rotate(grand, grand, node.left, node, node.right, parent);
            }
        }
    }

    /**
     * 更新高度
     */
    @Override
    protected void afterRotate(BinaryTree.Node<E> grand, BinaryTree.Node<E> parent, BinaryTree.Node<E> child) {
        super.afterRotate(grand, parent, child);
        //更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    @Override
    protected void rotate(BinaryTree.Node<E> r, BinaryTree.Node<E> b, BinaryTree.Node<E> c, BinaryTree.Node<E> d, BinaryTree.Node<E> e, BinaryTree.Node<E> f) {
        super.rotate(r, b, c, d, e, f);
        //更新高度
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    private static class AVLNode<E> extends BinaryTree.Node<E> {
        int height = 1;

        public AVLNode(E element, BinaryTree.Node<E> parent) {
            super(element, parent);
        }

        /**
         * @return 获取平衡因子
         */
        public int balanceFactor() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        /**
         * 更新高度
         */
        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public BinaryTree.Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight < rightHeight) {
                return right;
            }
            if (leftHeight > rightHeight) {
                return left;
            }
            return isLeftChild() ? left : right;
        }
    }
}
