package Giornate;

import java.util.Calendar;
import java.util.EnumMap;

/**
 * This class represents a day and has a Map that associates Bands to Integers: those represents the turns associated with the person's code.
 */
public class Day {
    // salvare il mese potrebbe essere inutile
    private final Months month;
    private final int number;
    private final WeekDays name;
    private final EnumMap<Bands, Integer> association;

    /**
     * Constructor of the class Giorno
     * @param number needs to be >0 && <=31
     */
    public Day(int number, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, number);
        this.month = Months.monthOf(month-1);
        this.number = number;
        this.name = WeekDays.dayOf(calendar.get(Calendar.DAY_OF_WEEK));
        association = new EnumMap<>(Bands.class);
        for (Bands bands : Bands.values()){
            association.put(bands,0);
        }
        //System.out.println(number + "/" + month + "/" + calendar.get(Calendar.YEAR));
        //System.out.println("Oggi è " + name + "(" + calendar.get(Calendar.DAY_OF_WEEK) + ")");
    }

    /**
     * @return the number of the day
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the name of the day
     */
    public WeekDays getName() {
        return name;
    }

    /**
     * @return the map with every association
     */
    private EnumMap<Bands, Integer> getAssociation() {
        return association;
    }

    /**
     * This method modify a band association - overwrite any other association - if the same person were already selected for the band, it is removed
     * @param band to modify
     * @param code of the person to insert
     * @return true if the band is assigned, false otherwise
     */
    public boolean modifyBand(Bands band, int code){
        if(association.get(band) != code){
            association.put(band,code);
            return true;
        }
        association.put(band,0);
        return false;
    }

    /**
     * Probably useless, but if it works don't touch it i guess? It should be identical to the modifyBand method - but its used for availability
     * @param band to change
     * @return true if assigned (1), false otherwise
     */
    public boolean switchBand(Bands band){
        if(association.get(band) == 0){
            association.put(band,1);
            return true;
        }
        association.put(band,0);
        return false;
    }

    /**
     * returns the working person based on the given Bands
     * @param band requested turn
     * @return the code of the person
     */
    public int getCode(Bands band){
        return association.get(band);
    }

    /**
     * This method checks if a person works on that day
     * @param code of the person
     * @return true if the person is assigned to one or more of the bands
     */
    public boolean doesWork(int code){
        return association.containsValue(code);
    }

    public boolean doesNight(int code){
        return association.get(Bands.F5).equals(code);
    }

    /**
     * This method checks if a band is assigned
     * @param b the asked band
     * @return the string "si" if the band is taken, "no" otherwise
     */
    public String checkBand(Bands b){
        if(association.get(b) != 0) return "si";
        return "no";
    }

    /**
     * This method is used to print stuff - It's used to print availability
     * @return a string containing name, number and every association of the day.
     */
    public String toStringAvailability() {
        String result = "\n" + name.toString() + " " + number;
        for (Bands bands : Bands.values()){
            result = result.concat("\nBanda " + bands + ": " + checkBand(bands));
        }
        return result;
    }

    /**
     * This method is used to print stuff - It's used to print holidays
     * @return a string containing name and number of the day.
     */
    public String toStringHolidays() {
        return "\n" + name.toString() + " " + number;
    }

    /**
     * Override of equals implementation for the Day class
     * @param day to compare
     * @return true if number, name and bands assignment of the two days are equals
     */
    @Override
    public boolean equals(Object day) {
        return (day instanceof Day && ((Day) day).getName().equals(this.name) && ((Day) day).getNumber() == this.number && ((Day) day).getAssociation().equals(this.association));
    }
}