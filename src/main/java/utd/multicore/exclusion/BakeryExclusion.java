package utd.multicore.exclusion;

import utd.multicore.Main;

import java.util.Arrays;

public class BakeryExclusion extends Exclusion {
    private final int[] token;
    private final boolean[] flag;

    public BakeryExclusion(int n) {
        super(n);
        this.token = new int[n];
        Arrays.fill(this.token, 0);
        this.flag = new boolean[n];
        Arrays.fill(this.flag, false);
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

    private int tokenMax() {
        synchronized (this.token) {
            int m = this.token[0];
            for (int i = 1; i < this.getN(); i++) m = Math.max(m, this.token[i]);
            return m;
        }
    }
}
