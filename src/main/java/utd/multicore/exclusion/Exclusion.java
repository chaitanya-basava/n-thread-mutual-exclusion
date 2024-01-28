package utd.multicore.exclusion;

public abstract class Exclusion {
    private final int n;

    protected Exclusion(int n) {
        this.n = n;
    }

    public abstract void lock(int id);
    public abstract void unlock(int id);

    public int getN() {
        return this.n;
    }
}
