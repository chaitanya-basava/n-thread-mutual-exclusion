package utd.multicore;

import utd.multicore.cs.CriticalSection;
import utd.multicore.exclusion.Exclusion;

public class Actor implements Runnable {
    private final int id;
    private final int csCount;
    private final Exclusion exclusion;
    private final CriticalSection cs;

    public Actor(int id, int csCount, Exclusion exclusion, CriticalSection cs) {
        this.id = id;
        this.csCount = csCount;
        this.exclusion = exclusion;
        this.cs = cs;
    }

    @Override
    public void run() {
        for(int i = 0; i < this.csCount; i++) {
            this.exclusion.lock(this.id);
            this.cs.execute();
            this.exclusion.unlock(this.id);
        }
    }
}
