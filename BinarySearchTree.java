// binary search tree.
import java.util.*;

class tNode {
    int key;
    tNode left, right;

    public tNode(int item) {
        key = item;
        left = right = null;
    }
}

class BST {
    tNode root;

    // Inserting into BST.
    tNode insertRec(tNode root, int key) {
        if (root == null) {
            return new tNode(key);
        }

        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);

        return root;
    }

    void insert(int key) {
        root = insertRec(root, key);
    }

    // Building balanced BST using middle-first strategy.
    void buildBalanced(int start, int end) {
        if (start > end) return;

        int mid = (start + end) / 2;
        insert(mid);

        buildBalanced(start, mid - 1);
        buildBalanced(mid + 1, end);
    }

    // Check if BST.
    boolean isBST() {
        return isBSTUtil(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    boolean isBSTUtil(tNode node, int min, int max) {
        if (node == null) return true;

        if (node.key <= min || node.key >= max)
            return false;

        return isBSTUtil(node.left, min, node.key) &&
               isBSTUtil(node.right, node.key, max);
    }

    // Delete a node
    tNode deleteRec(tNode root, int key) {
        if (root == null) return root;

        if (key < root.key)
            root.left = deleteRec(root.left, key);
        else if (key > root.key)
            root.right = deleteRec(root.right, key);
        else {
            // Node with one or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // Node with two children
            root.key = minValue(root.right);
            root.right = deleteRec(root.right, root.key);
        }
        return root;
    }

    int minValue(tNode root) {
        int min = root.key;
        while (root.left != null) {
            root = root.left;
            min = root.key;
        }
        return min;
    }

    void delete(int key) {
        root = deleteRec(root, key);
    }

    // Remove all even numbers
    void removeEvens(int max) {
        for (int i = 2; i <= max; i += 2) {
            delete(i);
        }
    }
}

public class tryBST {

    // Compute average
    public static double average(long[] arr) {
        double sum = 0;
        for (long val : arr) sum += val;
        return sum / arr.length;
    }

    // Compute standard deviation
    public static double stdDev(long[] arr, double mean) {
        double sum = 0;
        for (long val : arr) {
            sum += Math.pow(val - mean, 2);
        }
        return Math.sqrt(sum / arr.length);
    }

    public static void main(String[] args) {

        int n = 15; // Adjust this so time > 1000ms
        int max = (int)Math.pow(2, n) - 1;
        int repetitions = 30;

        long[] buildTimes = new long[repetitions];
        long[] deleteTimes = new long[repetitions];

        for (int i = 0; i < repetitions; i++) {

            BST tree = new BST();

            // --- Timing: Build tree ---
            long startBuild = System.currentTimeMillis();

            tree.buildBalanced(1, max);

            long endBuild = System.currentTimeMillis();
            buildTimes[i] = endBuild - startBuild;

            // Verify BST
            if (!tree.isBST()) {
                System.out.println("Tree is NOT a BST!");
                return;
            }

            // --- Timing: Delete evens ---
            long startDelete = System.currentTimeMillis();

            tree.removeEvens(max);

            long endDelete = System.currentTimeMillis();
            deleteTimes[i] = endDelete - startDelete;
        }

        // Compute stats
        double avgBuild = average(buildTimes);
        double stdBuild = stdDev(buildTimes, avgBuild);

        double avgDelete = average(deleteTimes);
        double stdDelete = stdDev(deleteTimes, avgDelete);

        // Display results
        System.out.println("\nRESULTS TABLE");
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-20s %-10s %-15s %-15s\n",
                "Method", "n", "Avg Time (ms)", "Std Dev");

        System.out.printf("%-20s %-10d %-15.2f %-15.2f\n",
                "Populate tree", n, avgBuild, stdBuild);

        System.out.printf("%-20s %-10d %-15.2f %-15.2f\n",
                "Remove evens", n, avgDelete, stdDelete);
    }
}
