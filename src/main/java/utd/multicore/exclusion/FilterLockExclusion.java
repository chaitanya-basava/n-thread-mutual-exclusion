package utd.multicore.exclusion;

public class FilterLockExclusion extends Exclusion {
    private static volatile GeneralizedPetersonLockExclusion[] level;

    public FilterLockExclusion(int n) {
        super(n);
        level = new GeneralizedPetersonLockExclusion[n];
        for(int i = 0; i < n; i++) level[i] = new GeneralizedPetersonLockExclusion(n);
    }

    @Override
    public void lock(int id) {
        for(int i = 1; i < this.getN(); i++) level[i].lock(id);
    }

    @Override
    public void unlock(int id) {
        for(int i = this.getN() - 1; i > 0; i--) level[i].unlock(id);
    }
}
