package utd.multicore.exclusion;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public class SharedFilterLockExclusion extends Exclusion {
    private static volatile AtomicIntegerArray level;
    private static volatile AtomicIntegerArray victim;

    public SharedFilterLockExclusion(int n) {
        super(n);
        level = new AtomicIntegerArray(n);
        victim = new AtomicIntegerArray(n);
    }

    @Override
    public void lock(int id) {
        for(int i = 1; i < this.getN(); i++) {
            level.set(id, i);
            victim.set(i, id);
            final int finalI = i;
            while (IntStream.range(0, this.getN()).anyMatch(k -> k != id && level.get(k) >= finalI)
                    && victim.get(i) == id);
        }
    }

    @Override
    public void unlock(int id) {
        level.set(id, 0);
    }
}
