package utd.multicore.exclusion;

import utd.multicore.Main;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GeneralizedPetersonLockExclusion extends Exclusion {
    private final boolean[] flag;
    private int victim;

    protected GeneralizedPetersonLockExclusion(int n) {
        super(n);
        this.victim = -1;
        this.flag = new boolean[n];
        Arrays.fill(this.flag, false);
    }

    @Override
    public void lock(int id) {
        this.flag[id] = true;
        this.victim = id;
        while (IntStream.range(0, this.getN()).anyMatch(k -> k != id && this.flag[k])
                && victim == id) Main.sleep(Main.SLEEP);
    }

    @Override
    public void unlock(int id) {
        this.flag[id] = false;
    }
}
