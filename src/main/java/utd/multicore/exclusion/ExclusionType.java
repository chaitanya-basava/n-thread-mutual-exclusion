package utd.multicore.exclusion;

public enum ExclusionType {
    FILTER_LOCK_1(1, FilterLockExclusion.class),
    FILTER_LOCK_2(2, SharedFilterLockExclusion.class),
    TOURNAMENT_TREE(3, TournamentTreeExclusion.class),
    BAKERY(4, BakeryExclusion.class);

    private final int type;
    private final Class<? extends Exclusion> clazz;

    ExclusionType(int type, Class<? extends Exclusion> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static Class<? extends Exclusion> getClassByType(int type) {
        for (ExclusionType value : ExclusionType.values()) {
            if (value.type == type) {
                return value.clazz;
            }
        }
        throw new IllegalArgumentException("No class found for type: " + type);
    }
}
