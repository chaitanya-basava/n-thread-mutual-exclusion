package utd.multicore.exclusion;

public class BakeryExclusion extends Exclusion {
    private static volatile long[] token;
    private static volatile boolean[] flag;

    public BakeryExclusion(int n) {
        super(n);
        token = new long[n];
        flag = new boolean[n];
    }

    @Override
    public void lock(int id) {
        flag[id] = true;
        token[id] = this.tokenMax() + 1;
        flag[id] = false;

        for(int i = 0; i < this.getN(); i++) {
            if(i == id) continue;
            while (flag[i]);
            while (token[i] != 0 && (token[id] > token[i] ||
                                    (token[id] == token[i] && id > i)));
        }
    }

    @Override
    public void unlock(int id) {
        token[id] = 0;
    }

    private long tokenMax() {
        long m = Long.MIN_VALUE;
        for (int i = 0; i < this.getN(); i++) m = Math.max(m, token[i]);
        return m;
    }
}
