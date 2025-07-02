package Giornate;

import java.util.HashMap;
import java.util.Map;

// Tutto sto casino serve per avere un metodo che mi ritorna il valore corrispondente alla enumerazione passandogli un intero

/**
 * This enumeration represents the months.
 */
public enum Months {
    Gennaio (1), Febbraio (2), Marzo (3), Aprile (4), Maggio (5), Giugno (6), Luglio (7), Agosto (8), Settembre (9), Ottobre (10), Novembre (11), Dicembre (12);


    private final int m;

    /**
     * This is the map that contains the effective Integer-Months association
     */
    private static final Map<Integer, Months> map = new HashMap<>();

    Months(int m){
        this.m = m;
    }

    // Creates the map and associates every integer to every month
    static {
        for(Months months : Months.values()){
            map.put(months.m, months);
        }
    }

    /**
     * @param m number of the wanted month
     * @return the corresponding enumerated month
     */
    public static Months monthOf(int m){
        return map.get(m+1);
    }

    /**
     * @return the number of the given month
     */
    public int getMonth() {
        return m;
    }


    @Override
    public String toString() {
        return this.name();
    }
}
