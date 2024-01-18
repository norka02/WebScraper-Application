package pl.hubertmaka.project.enums;

import pl.hubertmaka.project.interfaces.Type;

import java.util.HashMap;
import java.util.Map;

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
    WEST_POMERANIAN("zachodniopomorskie");

    private final String polishName;
    private static final Map<String, VoivodeshipType> BY_LABEL = new HashMap<>();

    static {
        for (VoivodeshipType e : values()) {
            BY_LABEL.put(e.polishName, e);
        }
    }

    VoivodeshipType(String polishName) {
        this.polishName = polishName;
    }

    public static VoivodeshipType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    @Override
    public String getPolishName() {
        return this.polishName;
    }
}
