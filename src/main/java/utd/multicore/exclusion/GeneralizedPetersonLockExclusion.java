package utd.multicore.exclusion;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

public class GeneralizedPetersonLockExclusion extends Exclusion {
    private final AtomicIntegerArray flag;
    private final AtomicInteger victim;

    protected GeneralizedPetersonLockExclusion(int n) {
        super(n);
        this.victim = new AtomicInteger(-1);
        this.flag = new AtomicIntegerArray(n * 16);
    }

    @Override
    public void lock(int id) {
        this.flag.set(id * 16, 1);
        this.victim.set(id);
        while (IntStream.range(0, this.getN()).anyMatch(k -> k != id && this.flag.get(k * 16) == 1)
                && victim.get() == id);
    }

    @Override
    public void unlock(int id) {
        this.flag.set(id * 16, 0);
    }
}
