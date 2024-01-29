package utd.multicore.cs;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.Main;

public class CounterCriticalSection implements CriticalSection {
    static final Logger logger = LoggerFactory.getLogger(CounterCriticalSection.class);
    private int counter;
    private final ExponentialDistribution expDist;

    public CounterCriticalSection() {
        this.counter = 0;
        this.expDist = new ExponentialDistribution(5);
    }

    @Override
    public void execute() {
        this.counter = this.counter + 1;
        int sleepTime = (int) this.expDist.sample();
        logger.info("CS executed and count is: " + this.counter + " waiting for " + sleepTime);

        Main.sleep(sleepTime);
    }

    public String getDetails() {
        return String.valueOf(this.counter);
    }
}
