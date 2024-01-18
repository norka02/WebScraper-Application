package pl.hubertmaka.project.enums;

import java.util.HashMap;
import java.util.Map;

public enum CityType {
    WARSAW("warszawa"),
    KRAKOW("krakow"),
    LODZ("lodz"),
    WROCLAW("wroclaw"),
    POZNAN("poznan"),
    GDANSK("gdansk"),
    SZCZECIN("szczecin"),
    BYDGOSZCZ("bydgoszcz"),
    LUBLIN("lublin"),
    KATOWICE("katowice"),
    BIALYSTOK("bialystok"),
    GDYNIA("gdynia"),
    CZESTOCHOWA("czestochowa"),
    RZESZOW("rzeszow"),
    GLIWICE("gliwice"),
    ZABRZE("zabrze"),
    OLSZTYN("olsztyn"),
    BIELSKO_BIALA("bielsko-biala"),
    BYTOM("bytom"),
    ZIELONA_GORA("zielona-gora"),
    ZIELONA_GORA_OLX("zielonagora"),
    KALISZ("kalisz"),
    LEGNICA("legnica"),
    WALBRZYCH("walbrzych"),
    JELENIA_GORA("jelenia-gora"),
    LUBIN("lubin"),
    INOWROCLAW("inowroclaw"),
    WLOCLAWEK("wloclawek"),
    GRUDZIADZ("grudziadz"),
    CHELM("chelm"),
    ZAMOSC("zamosc"),
    BIALA_PODLASKA("biala-podlaska"),
    PULAWY("pulawy"),
    NOWA_SOL("nowa-sol"),
    ZARY("zary"),
    ZAGAN("zagan"),
    PIOTRKOW_TRYBUNALSKI("piotrkow-trybunalski"),
    PABIANICE("pabianice"),
    TOMASZOW_MAZOWIECKI("tomaszow-mazowiecki"),
    BECHATOW("belchatow"),
    NOWY_SACZ("nowy-sacz"),
    OSWIECIM("oswiecim"),
    CHRZANOW("chrzanow"),
    OLKUSZ("olkusz"),
    NOWY_TARG("nowy-targ"),
    BOCHNIA("bochnia"),
    PRUSZKOW("pruszkow"),
    OSTROLEKA("ostroleka"),
    LEGIONOWO("legionowo"),
    CIECHANOW("ciechanow"),
    KEDZIERZYN_KOZLE("kedzierzyn-kozle"),
    NYSA("nysa"),
    BRZEG("brzeg"),
    KLUCZBORK("kluczbork"),
    MIELEC("mielec"),
    TARNOBRZEG("tarnobrzeg"),
    SANOK("sanok"),
    JASLO("jaslo"),
    AUGUSTOW("augustow"),
    BIELSK_PODLASKI("bielsk-podlaski"),
    TCZEW("tczew"),
    STAROGARD_GDANSKI("starogard-gdanski"),
    WEJHEROWO("wejherowo"),
    RUMIA("rumia"),
    OSTROWIEC_SWIETOKRZYSKI("ostrowiec-swietokrzyski"),
    STARACHOWICE("starachowice"),
    SANDOMIERZ("sandomierz"),
    SKARZYSKO_KAMIENNA("skarzysko-kamienna"),
    KONSKIE("konskie"),
    ELBLAG("elblag"),
    ELK("elk"),
    OSTRODA("ostroda"),
    ILAWA("ilawa"),
    KETRZYN("ketrzyn"),
    SZCZYTNO("szczytno"),
    GIZYCKO("gizycko"),
    SWINOUJSCIE("swinoujscie");

    private final String polishName;
    private static final Map<String, CityType> BY_LABEL = new HashMap<>();

    static {
        for (CityType e : values()) {
            BY_LABEL.put(e.polishName, e);
        }
    }

    CityType(String polishName) {
        this.polishName = polishName;
    }

    public static CityType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public String getPolishName() {
        return this.polishName;
    }
}
