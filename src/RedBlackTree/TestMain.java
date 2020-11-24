package RedBlackTree;


import RedBlackTree.printer.BinaryTrees;

public class TestMain {

    public static void main(String[] args) {
        RBTree<Integer> rbt = new RBTree<>();
        int[] data = new int[]{
                55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        };
        for (int i = 0; i < data.length; i++) {
            rbt.add(data[i]);
        }
        BinaryTrees.println(rbt);
        for (int i = 0; i < data.length; i++) {
            rbt.remove(data[i]);
            System.out.println("--------------------------");
            System.out.println("【"+data[i]+"】");
            BinaryTrees.println(rbt);
        }
    }
}
