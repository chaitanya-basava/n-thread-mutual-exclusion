package utd.multicore.exclusion;

import utd.multicore.Main;
import java.util.stream.IntStream;

public class SharedFilterLockExclusion extends Exclusion {
    private final int[] level;
    private final int[] victim;

    public SharedFilterLockExclusion(int n) {
        super(n);
        this.level = new int[n];
        this.victim = new int[n];
    }

    @Override
    public void lock(int id) {
        for(int i = 1; i < this.getN(); i++) {
            level[id] = i;
            victim[i] = id;
            final int finalI = i;
            while (IntStream.range(0, this.getN()).anyMatch(k -> k != id && this.level[k] >= finalI)
                    && victim[i] == id) Main.sleep(Main.SLEEP);
        }
    }

    @Override
    public void unlock(int id) {
        this.level[id] = 0;
    }
}
