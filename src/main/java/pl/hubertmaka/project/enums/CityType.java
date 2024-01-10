package pl.hubertmaka.project.enums;

import pl.hubertmaka.project.interfaces.Type;

public enum CityType implements Type {
    WARSAW("warszawa"),
    KRAKOW("krakow"),
    LODZ("lodz"),
    WROCLAW("wroclaw"),
    POZNAN("poznan"),
    GDANSK("gdansk"),
    SZCZECIN("szczecin"),
    BYDGOSZCZ("bydgoszcz"),
    LUBLIN("lublin"),
    KATOWICE("katowice");

    private final String polishName;

    CityType(String name) {
        this.polishName = name;
    }
    @Override
    public String getPolishName() {
        return this.polishName;
    }
}
