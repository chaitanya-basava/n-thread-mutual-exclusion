package utd.multicore.exclusion;

import java.util.LinkedList;
import java.util.Queue;

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
    }

    private final TournamentTree root;

    private TournamentTree buildTree(int start, int end) {
        TournamentTree node = new TournamentTree(start, end);
        if (start < end-1) {
            int mid = start + (end - start) / 2;
            node.left = buildTree(start, mid);
            node.right = buildTree(mid + 1, end);
        }
        return node;
    }

    private void printTree() {
        Queue<TournamentTree> queue = new LinkedList<>();
        queue.add(this.root);
        while(!queue.isEmpty()) {
            TournamentTree temp = queue.poll();
            assert temp != null;
            System.out.print(temp + " ");
            if(temp.left != null) queue.add(temp.left);
            if(temp.right != null) queue.add(temp.right);
        }
        System.out.println();
    }

    public TournamentTreeExclusion(int n) {
        super(n);
        this.root = this.buildTree(0, n-1);
        this.printTree();
    }

    @Override
    public void lock(int id) {
        this.traverseForward(this.root, id, true);
    }

    @Override
    public void unlock(int id) {
        this.traverseForward(this.root, id, false);
    }

    private void traverseForward(TournamentTree node, int k, boolean lockOp) {
        if (node == null || k < node.low || k > node.high) return;
        if (node.high - node.low <= 1) {
            if(node.low == k) {
                if(lockOp) node.lock(0);
                else node.unlock(0);
            }
            else {
                if(lockOp) node.lock(1);
                else node.unlock(1);
            }
        }
        else if (node.left != null && k <= node.left.high) {
            if(!lockOp) node.unlock(0);
            traverseForward(node.left, k, lockOp);
            if(lockOp) node.lock(0);
        } else if (node.right != null) {
            if(!lockOp) node.unlock(1);
            traverseForward(node.right, k, lockOp);
            if(lockOp) node.lock(1);
        }
    }
}
