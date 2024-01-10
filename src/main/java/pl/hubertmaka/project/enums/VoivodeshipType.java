package pl.hubertmaka.project.enums;

import pl.hubertmaka.project.interfaces.Type;

public enum VoivodeshipType implements Type {
    LOWER_SILESIA("dolnoslaskie"),
    KUYAVIA_POMERANIA("kujawsko--pomorskie"),
    LUBLIN("lubelskie"),
    LUBLUSZ("lubuskie"),
    LODZ("lodzkie"),
    LESSER_POLAND("malopolskie"),
    MASOVIAN("mazowieckie"),
    OPOLSKIE("opolskie"),
    SUBCARPATHIAN("podkarpackie"),
    PODLASKIE("podlaskie"),
    POMERANIAN("pomorskie"),
    SILESIA("slaskie"),
    SWIETOKRZYSKIE("swietokrzyskie"),
    WARMIA_MASURIA("warminsko--mazurskie"),
    GREATER_POLAND("wielkopolskie"),
    WEST_POMERANIAN("zachodniopomorskie"),
    ALL("cala-polska");

    private final String polishName;

    VoivodeshipType(String name) {
        this.polishName = name;
    }

    @Override
    public String getPolishName() {
        return this.polishName;
    }
}
