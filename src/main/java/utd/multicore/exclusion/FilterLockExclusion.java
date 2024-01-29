package utd.multicore.exclusion;

public class FilterLockExclusion extends Exclusion {
    private final GeneralizedPetersonLockExclusion[] level;

    public FilterLockExclusion(int n) {
        super(n);
        this.level = new GeneralizedPetersonLockExclusion[n];
        for(int i = 0; i < n; i++) this.level[i] = new GeneralizedPetersonLockExclusion(n);
    }

    @Override
    public void lock(int id) {
        for(int i = 1; i < this.getN(); i++) this.level[i].lock(id);
    }

    @Override
    public void unlock(int id) {
        for(int i = this.getN() - 1; i > 0; i--) this.level[i].unlock(id);
    }
}
