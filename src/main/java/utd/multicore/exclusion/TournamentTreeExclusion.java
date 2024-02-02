package utd.multicore.exclusion;

import java.util.Stack;

public class TournamentTreeExclusion extends Exclusion {
    private static class TournamentTree {
        private final GeneralizedPetersonLockExclusion gpl;
        protected TournamentTree left;
        protected TournamentTree right;
        private final int low, high;

        TournamentTree(int low, int high) {
            this.low = low;
            this.high = high;
            this.gpl = new GeneralizedPetersonLockExclusion(2);
            this.left = null;
            this.right = null;
        }

        protected void lock(int side) {
            this.gpl.lock(side);
        }

        protected void unlock(int side) {
            this.gpl.unlock(side);
        }

        @Override
        public String toString() {
            return "(" + low + "-" + high + ")";
        }

        public void printTree() { // inorder
            Stack<TournamentTree> stack = new Stack<>();
            stack.add(this);
            while(!stack.isEmpty()) {
                TournamentTree temp = stack.pop();
                System.out.print(temp + " ");
                if(temp.right != null) stack.add(temp.right);
                if(temp.left != null) stack.add(temp.left);
            }
            System.out.println();
        }
    }

    private final TournamentTree root;

    private TournamentTree buildTree(int start, int end) {
        TournamentTree node = new TournamentTree(start, end);
        if (end - start > 1) {
            int mid = start + (end - start) / 2;
            node.left = buildTree(start, mid);
            node.right = buildTree(mid + 1, end);
        }
        return node;
    }

    public TournamentTreeExclusion(int n) {
        super(n);
        this.root = this.buildTree(0, n-1);
        this.root.printTree();
    }

    @Override
    public void lock(int id) {
        this.traverseTree(this.root, id, true);
    }

    @Override
    public void unlock(int id) {
        this.traverseTree(this.root, id, false);
    }

    private void traverseTree(TournamentTree node, int k, boolean lockOp) {
        if (node == null || k < node.low || k > node.high) return;

        int childIndex = (node.high - node.low <= 1) ? (k == node.low ? 0 : 1) : (node.left != null && k <= node.left.high ? 0 : 1);
        if (!lockOp) node.unlock(childIndex);

        if (node.high - node.low > 1) {
            traverseTree(childIndex == 0 ? node.left : node.right, k, lockOp);
        }

        if(lockOp) node.lock(childIndex);
    }
}
