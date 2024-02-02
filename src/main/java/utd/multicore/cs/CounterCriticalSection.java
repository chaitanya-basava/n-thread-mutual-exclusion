package utd.multicore.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterCriticalSection implements CriticalSection {
    static final Logger logger = LoggerFactory.getLogger(CounterCriticalSection.class);
    private int counter;

    public CounterCriticalSection() {
        this.counter = 0;
    }

    @Override
    public void execute() {
        this.counter = this.counter + 1;
        logger.info("CS executed and count is: " + this.counter);
    }

    public String getDetails() {
        return String.valueOf(this.counter);
    }
}
