package Giornate;

import java.util.HashMap;
import java.util.Map;

// Tutto sto casino serve per avere un metodo che mi ritorna il valore corrispondente alla enumerazione passandogli un intero

/**
 * This enumeration is used to save the five types of turns: 9:30-15:00, 9:00-15:30, 15:00-21:00, 15:30-21:30, 21:00-9:30
 */
public enum Bands {
    F1 (1), F2 (2),F3 (3),F4 (4),F5 (5);


    private final int band;

    /**
     * This is the map that contains the effective Integer-Bands association
     */
    private static final Map<Integer, Bands> map = new HashMap<>();

    Bands(int b){
        this.band = b;
    }

    // Creates the map and associates every integer to every day
    static {
        for(Bands b : Bands.values()){
            map.put(b.band, b);
        }
    }

    /**
     * @param l number of the wanted day
     * @return the corresponding enumerated day
     */
    public static Bands bandOf(int l){
        return map.get(l);
    }

    /**
     * @return the number of the given day
     */
    public int getNumber() {
        return band;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
