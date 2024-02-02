package utd.multicore;

import utd.multicore.cs.CriticalSection;
import utd.multicore.exclusion.Exclusion;

public class Actor implements Runnable {
    private final int id;
    private final int csCount;
    private final Exclusion exclusion;
    private final CriticalSection cs;
    private final long[] latency;
    private final long[] turnaroundTime;

    public Actor(int id, int csCount, Exclusion exclusion, CriticalSection cs) {
        this.id = id;
        this.csCount = csCount;
        this.exclusion = exclusion;
        this.cs = cs;
        this.latency = new long[this.csCount];
        this.turnaroundTime = new long[this.csCount];
    }

    @Override
    public void run() {
        for(int i = 0; i < this.csCount; i++) {
            long processRequested = System.currentTimeMillis();
            this.exclusion.lock(this.id);
            long processStarted = System.currentTimeMillis();
            this.cs.execute();
            long processCompleted = System.currentTimeMillis();
            this.exclusion.unlock(this.id);

            this.latency[i] = processStarted - processRequested;
            this.turnaroundTime[i] = processCompleted - processRequested;
        }
    }

    public long getLatencyAtI(int i) {
        return this.latency[i];
    }

    public long getTurnaroundTimeAtI(int i) {
        return this.turnaroundTime[i];
    }
}
