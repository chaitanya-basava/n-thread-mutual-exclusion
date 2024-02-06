package utd.multicore.exclusion;

import java.util.stream.IntStream;

public class SharedFilterLockExclusion extends Exclusion {
    private static volatile int[] level;
    private static volatile int[] victim;

    public SharedFilterLockExclusion(int n) {
        super(n);
        level = new int[n];
        victim = new int[n];
    }

    @Override
    public void lock(int id) {
        for(int i = 1; i < this.getN(); i++) {
            level[id] = i;
            victim[i] = id;
            final int finalI = i;
            while (IntStream.range(0, this.getN()).anyMatch(k -> k != id && level[k] >= finalI)
                    && victim[i] == id);
        }
    }

    @Override
    public void unlock(int id) {
        level[id] = 0;
    }
}
