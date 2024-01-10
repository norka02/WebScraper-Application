package pl.hubertmaka.project.enums;

public enum Limit {
    LIMIT_24(24),
    LIMIT_32(36),
    LIMIT_48(48),
    LIMIT_72(72);

    private final int limit;

    Limit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return this.limit;
    }


}
