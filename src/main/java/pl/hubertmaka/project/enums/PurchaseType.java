package pl.hubertmaka.project.enums;

import pl.hubertmaka.project.interfaces.Type;

public enum PurchaseType implements Type {
    FOR_SALE("sprzedaz"),
    ON_RENT("wynajem");

    private final String polishName;

    PurchaseType(String name) {
        this.polishName = name;
    }

    @Override
    public String getPolishName() {
        return this.polishName;
    }
}
