package utd.multicore.exclusion;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

public class BakeryExclusion extends Exclusion {
    private static volatile AtomicLongArray token;
    private static volatile AtomicIntegerArray flag;

    public BakeryExclusion(int n) {
        super(n);
        token = new AtomicLongArray(n);
        flag = new AtomicIntegerArray(n);
    }

    @Override
    public void lock(int id) {
        flag.set(id, 1);
        token.set(id, this.tokenMax() + 1);
        flag.set(id, 0);

        for(int i = 0; i < this.getN(); i++) {
            if(i == id) continue;
            while (flag.get(i) == 1);
            while (token.get(i) != 0 && (token.get(id) > token.get(i) ||
                                    (token.get(id) == token.get(i) && id > i)));
        }
    }

    @Override
    public void unlock(int id) {
        token.set(id, 0);
    }

    private long tokenMax() {
        long m = Long.MIN_VALUE;
        for (int i = 0; i < this.getN(); i++) m = Math.max(m, token.get(i));
        return m;
    }
}
