package tree.binarytree;

import tree.TreeNode;

public class LowestCommonAncestor {
    private static TreeNode findLCA(TreeNode root, TreeNode n1, TreeNode n2) {
        if(root == null){
            return root;
        }
        if(root == n1 || root == n2){
            return root;
        }
        TreeNode left = findLCA(root.left, n1, n2);
        TreeNode right = findLCA(root.right, n1, n2);
        if(left != null && right != null){
            return root;
        }
        return left!=null ? left : right;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.left.left = new TreeNode(8);

        TreeNode n1 = root.right.right;
        TreeNode n2 = root.right.left.left;
        TreeNode lcaNode = findLCA(root, n1, n2);
        System.out.println(lcaNode.data);
    }
}
