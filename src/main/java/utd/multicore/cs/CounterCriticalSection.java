package utd.multicore.cs;

public class CounterCriticalSection implements CriticalSection {
    private int counter;

    public CounterCriticalSection() {
        this.counter = 0;
    }

    @Override
    public void execute() {
        this.counter = this.counter + 1;
    }

    public String getDetails() {
        return String.valueOf(this.counter);
    }
}
