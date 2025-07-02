package Giornate;

import java.util.HashMap;
import java.util.Map;

public enum WeekDays {
    LUNEDI (1), MARTEDI (2), MERCOLEDI (3), GIOVEDI (4), VENERDI (5), SABATO (6), DOMENICA (0);

    private final int day;
    private static final Map<Integer, WeekDays> map = new HashMap<>();

    WeekDays(int l){
        this.day = l;
    }

    // Creates the map and associates every integer to every day
    static {
        for(WeekDays gs : WeekDays.values()){
            map.put(gs.day, gs);
        }
    }

    /**
     * @param l number of the wanted day
     * @return the corresponding enumerated day
     */
    public static WeekDays dayOf(int l){
        return map.get(l-1);
    }

    /**
     * @return the number of the given day
     */
    public int getNumber() {
        return day;
    }

    @Override
    public String toString() {
        switch (this.name()){
            case "LUNEDI" -> {
                return "LUNEDÌ";
            }
            case "MARTEDI" -> {
                return "MARTEDÌ";
            }
            case "MERCOLEDI" -> {
                return "MERCOLEDÌ";
            }
            case "GIOVEDI" -> {
                return "GIOVEDÌ";
            }
            case "VENERDI" -> {
                return "VENERDÌ";
            }
            case "SABATO" -> {
                return "SABATO";
            }
            case "DOMENICA" -> {
                return "DOMENICA";
            }
            default -> {
                return "errore";
            }
        }
    }
}
