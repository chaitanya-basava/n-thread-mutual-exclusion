package utd.multicore.cs;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import utd.multicore.Main;

public class CounterCriticalSection implements CriticalSection {
    private int counter;
    private final ExponentialDistribution expDist;

    public CounterCriticalSection() {
        this.counter = 0;
        this.expDist = new ExponentialDistribution(5);
    }

    @Override
    public int execute() {
        this.counter = this.counter + 1;
        int sleepTime = (int) this.expDist.sample();
        Main.sleep(sleepTime);

        return sleepTime;
    }

    public String getDetails() {
        return String.valueOf(this.counter);
    }
}
