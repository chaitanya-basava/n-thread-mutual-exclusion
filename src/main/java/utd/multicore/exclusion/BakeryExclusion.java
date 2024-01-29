package utd.multicore.exclusion;

import utd.multicore.Main;

public class BakeryExclusion extends Exclusion {
    private final long[] token;
    private final boolean[] flag;

    public BakeryExclusion(int n) {
        super(n);
        this.token = new long[n];
        this.flag = new boolean[n];
    }

    @Override
    public void lock(int id) {
        this.flag[id] = true;
        this.token[id] = this.tokenMax() + 1;
        this.flag[id] = false;

        for(int i = 0; i < this.getN(); i++) {
            if(i == id) continue;
            while (this.flag[i]) Main.sleep(Main.SLEEP);
            while (
                    this.token[i] != 0 && (this.token[id] > this.token[i] ||
                                    (this.token[id] == this.token[i] && id > i))
            ) Main.sleep(Main.SLEEP);
        }
    }

    @Override
    public void unlock(int id) {
        this.token[id] = 0;
    }

    private long tokenMax() {
        long m = Long.MIN_VALUE;
        for (int i = 0; i < this.getN(); i++) m = Math.max(m, this.token[i]);
        return m;
    }
}
