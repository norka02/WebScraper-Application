package pl.hubertmaka.project.enums;

import pl.hubertmaka.project.interfaces.Type;

public enum PropertyType implements Type {
    APARTMENTS_OLX("mieszkania"),
    APARTMENTS("mieszkanie"),
    STUDIOS("kawalerka"),
    HOUSES("dom"),
    INVESTMENT("inwestycja"),
    ROOM("pokoj"),
    PLOT("dzialka"),
    BUSINESS_PREMISES("lokal"),
    WAREHOUSE("haleimagazyny"),
    GARAGE("garaz");

    private final String polishName;

    PropertyType(String name) {
        this.polishName = name;
    }

    @Override
    public String getPolishName() {
        return this.polishName;
    }
}
