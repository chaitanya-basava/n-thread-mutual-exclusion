package utd.multicore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.cs.CriticalSection;
import utd.multicore.exclusion.Exclusion;

public class Actor extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Actor.class);

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

            int executionTime = this.cs.execute();
            logger.info("CS executed by " + id + " and count is: " +
                    this.cs.getDetails() + " waiting for " + executionTime);

            this.exclusion.unlock(this.id);
        }
    }
}
