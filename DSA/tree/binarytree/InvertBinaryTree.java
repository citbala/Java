package tree.binarytree;

import tree.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InvertBinaryTree {
    private static void invertTree(TreeNode root){
        if(root == null){
            return;
        }
        invertTree(root.left);
        invertTree(root.right);

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
    }

    static void printLevelOrder(TreeNode root){
        List<String> list = traverseLevelOrder(root);
        removeTrailingNullValues(list);
        //print nodes in level order
        //printLevelOrderIterative(list);

        //removeTrailingNullValuesStream(list);
        // java 8 way to print nodes in level order
        String result = list.stream()
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println(result);
    }

    private static List<String> traverseLevelOrder(TreeNode root) {
        if(root == null){
            System.out.println("[]");
            return null;
        }
        List<String> list = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty()){
            TreeNode curr = queue.poll();
            if(curr != null){
                list.add(Integer.toString(curr.data));//1,2,3,4,5
                queue.add(curr.left);
                queue.add(curr.right);
            }
            else {
                list.add("N");
            }
        }
        return list;
    }

    private static void removeTrailingNullValues(List<String> list) {
        //Traditional way of removing trails
        while(!list.isEmpty() && list.get(list.size()-1).equals("N"))
            list.remove(list.size()-1);
        //Scroll to end to see stream variant of same logic,
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        System.out.println("Given Binary Tree:");
        printLevelOrder(root);
        invertTree(root);
        System.out.println("Inverted Binary Tree");
        printLevelOrder(root);
    }

    private static void printLevelOrderIterative(List<String> list) {
        System.out.print("[");
        for(int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));

            if(i+1 != list.size()){
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    private static void removeTrailingNullValuesStream(List<String> list) {
        /*int lastNonNullIndex = IntStream.range(0, list.size())
                .filter(index -> !"N".equals(list.get(index))) //Get non null INDEXES from list
                .reduce((first, second) -> second)//keep the last matching index
                .orElse(-1);
        return list.subList(0, lastNonNullIndex + 1);*/

        /* Explanation for reduce()
         * reduce(first, second) -> second ;means ignore first and consider only second
         * */

        int lastNonNullIndex = IntStream.range(0, list.size())
                .filter(index -> !"N".equals(list.get(index)))
                .reduce((first, second) -> second)
                .orElse(-1);
        if(lastNonNullIndex+1 < list.size()){
            list.subList(lastNonNullIndex+1, list.size()).clear();
        }
        //Combining both statements to 1, Same logic
        /*return list.subList(0, IntStream.range(0, list.size())
                .filter(index -> !"N".equals(list.get(index)))
                .reduce((first, second) -> second)
                .orElse(-1)+1);*/
    }
}
