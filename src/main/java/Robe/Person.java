package Robe;

import Giornate.Bands;
import Giornate.Day;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class represents a person
 */
public class Person {
    private final int code;
    private final String name;
    private final String surname;
    private int nights=0;
    private float totalHours;
    private float weekHours;
    private float actualHours;
    private float nightHours;
    private boolean doesNight;
    private List<Day> availability;
    private List<Day> holidays;

    /**
     * This is the constructor of the class
     * @param name of the person
     * @param surname of the person
     * @param weekHours of the person
     * @param code of the person
     * @param nightHours of the person
     * @param doesNight of the person
     */
    @JsonCreator
    public Person(@JsonProperty("name") String name, @JsonProperty("surname") String surname, @JsonProperty("hours") float weekHours, @JsonProperty("code") int code, @JsonProperty("nightHours") float nightHours, @JsonProperty("doesNight") boolean doesNight) {
        this.name = name;
        this.surname = surname;
        this.weekHours = weekHours;
        this.code = code;
        this.actualHours = 0;
        this.nightHours = nightHours;
        this.doesNight = doesNight;
        availability = new ArrayList<>();
        holidays = new ArrayList<>();
    }

    /**
     * @return the number of nights of the person
     */
    @JsonIgnore
    public int getNights() {
        return nights;
    }

    /**
     * @return the code of the person
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * @return the surname of the person
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return true if the person does work at night, false otherwise
     */
    @JsonProperty("doesNight")
    public boolean doesNights(){
        return doesNight;
    }

    /**
     * @param doesNight to set
     */
    public void setNights(boolean doesNight){
        this.doesNight = doesNight;
    }

    /**
     * @return a string containing "name surname"
     */
    @JsonIgnore
    public String getFullname(){
        return getName() + " " + getSurname();
    }

    /**
     * @return the actualHours of the person
     */
    @JsonIgnore
    public float getActualHours(){
        return this.actualHours;
    }

    /**
     * @return the totalHours of the person
     */
    @JsonIgnore
    public float getTotalHours(){
        return totalHours;
    }

    /**
     * @param totalHours to set to the person
     */
    public void setTotalHours(float totalHours) {
        this.totalHours = (int) totalHours;
    }

    /**
     * @return the weekHours of the person
     */
    @JsonProperty("hours")
    public float getWeekHours(){
        return this.weekHours;
    }

    /**
     * This method sets the weekHours and recalculate the totalHours
     * @param weekHours to set to the person
     * @param n number of days of the selected month
     */
    public void setWeekHours(float weekHours, int n){
        this.weekHours = weekHours;
        setTotalHours((this.weekHours)*( 4+ (float) (n%7)/7 ));
    }

    /**
     * @return the additive night hours of the person
     */
    @JsonProperty("nightHours")
    public float getNightHours(){
        return nightHours;
    }

    /**
     * This method is used to print the name of the worker in the calendar
     * @return a string containing "Name S(1)" with S(1) = first letter of the surname
     */
    @JsonIgnore
    public String getShortName(){
        String out = this.name;
        if(out.length()>8){
            out = out.substring(0,7) + ".";
        }
        return out + " " + this.surname.toUpperCase(Locale.ROOT).charAt(0) + ".";
    }

    /**
     * This method set the night hours and recalculate the night hours based on the number of nights of the person
     * @param nightHours to set
     */
    public void setNightHours(float nightHours) {
        float temp = this.nightHours-nightHours;
        actualHours = actualHours - (nights * temp);
        this.nightHours = nightHours;
    }

    /**
     * @return true if the person can still get turns
     */
    public boolean canWork(){
        return this.actualHours <= this.totalHours;
    }

    /**
     * This method adds hours of work for the worker
     * @param band hours of work to add to the person
     */
    public void addHours(Bands band){
        switch (band) {
            case F1 -> actualHours += 5.5;
            case F2 -> actualHours += 6.5;
            case F3, F4 -> actualHours += 6;
            case F5 -> {
                actualHours += (6.5 + nightHours);
                nights++;
            }
        }
    }

    /**
     * This method adds a given number of hours to the actualHours - not used
     * @param n to add
     */
    public void addSetHours(float n){
        actualHours += n;
    }

    /**
     * This method removes hours of work from the worker
     * @param band hours of work to remove to the person
     */
    public void removeHours(Bands band){
        switch (band) {
            case F1 -> actualHours -= 5.5;
            case F2 -> actualHours -= 6.5;
            case F3, F4 -> actualHours -= 6;
            case F5 -> {
                actualHours -= (6.5 + nightHours);
                nights--;
            }
        }
    }

    /**
     * This method adds the required hours of the equipe based on the given band
     * @param band to determine the number of hours
     */
    public void addEquipeHours(Bands band){
        // use when you remove a person from that turn
        switch (band) {
            case F1 -> this.actualHours += 2;
            case F2 -> this.actualHours += 2.5;
            case F3 -> this.actualHours += 0.5;
            default -> {}                                   // per coerenza e comprendibilità - se lavoravo fuori orario equipe devo tenerla
        }
    }

    /**
     * This method remove the required hours of the equipe based on the given band
     * @param band to determine the number of hours
     */
    public void removeEquipeHours(Bands band){
        // use when you add a person to that turn
        switch (band) {
            case F1 -> this.actualHours -= 2;
            case F2 -> this.actualHours -= 2.5;
            case F3 -> this.actualHours -= 0.5;
            default -> {}                                   // per coerenza e comprendibilità - se lavoravo fuori orario equipe devo tenerla
        }
    }

    /**
     * This method adds the required hours of the supervision based on the given band
     * @param band to determine the number of hours
     */
    public void addSupervisionHours(Bands band){
        switch (band) {
            case F3, F4, F5 -> {}
            default -> this.actualHours += 1;                                   // per coerenza e comprendibilità - se lavoravo fuori orario equipe devo tenerla
        }
    }

    /**
     * This method remove the required hours of the supervision based on the given band
     * @param band to determine the number of hours
     */
    public void removeSupervisionHours(Bands band){
        switch (band) {
            case F3, F4, F5 -> {}
            default -> this.actualHours -= 1;                                  // per coerenza e comprendibilità - se lavoravo fuori orario equipe devo tenerla
        }
    }
    /**
     * This methods adds a new availability to the list if it is new, otherwise it will remove it
     * @param daynumber added/removed period of time to the list where the given person can't work (can be for single bands)
     * @return true if added, false if removed
     */
    public boolean modifyAvailability(int daynumber, Bands bands, Calendario cal){
        for(Day day : availability){
            if(day.getNumber() == daynumber){
                return day.switchBand(bands);
            }
        }
        Day day = new Day(daynumber, cal.getMonth(), cal.getYear());
        day.switchBand(bands);
        availability.add(day);
        sortAvailability();
        return true;
    }

    /**
     * This methods adds a new holiday to the list if it is new, otherwise it will remove it
     * @param daynumber added/removed day to the list where the given person is on holiday (can be ONLY for entire days)
     * @return true if added, false if removed
     */
    public boolean modifyHolidays(int daynumber, Calendario cal){
        Day day = new Day(daynumber, cal.getMonth(), cal.getYear());
        if(!holidays.contains(day)){
            holidays.add(day);
            sortHolidays();
            return true;
        }
        else {
            holidays.remove(day);
            return false;
        }
    }



    /**
     * This method resets every modifiable attributes.
     */
    public void reset(){
        this.nights=0;
        this.actualHours=0;
        this.availability = new ArrayList<>();
        this.holidays = new ArrayList<>();
    }

    /**
     * This method checks if the person can work in the decided day and band, according to his availability and holidays.
     * @param daynumber to check
     * @param band to check
     * @return true if the person can work, false otherwise
     */
    public boolean isFree(int daynumber, Bands band){
        for(Day day : holidays){
            if(daynumber == day.getNumber()) return false;
        }
        for(Day day : availability){
            if(daynumber == day.getNumber() && day.checkBand(band).equals("si")) return false;
        }
        return true;
    }

    /**
     * This method checks if the person can work in the decided day and band, according to his availability.
     * @param daynumber to check
     * @param band to check
     * @return true if the person can work, false otherwise
     */
    public boolean hasAvailability(int daynumber, Bands band){
        for(Day day : availability){
            if(daynumber == day.getNumber() && day.checkBand(band).equals("si")) return false;
        }
        return true;
    }

    /**
     * This method checks if the person can work in the decided day and band, according to his availability.
     * @param daynumber to check
     * @return true if the person can work, false otherwise
     */
    public boolean hasHoliday(int daynumber){
        for(Day day : holidays){
            if(daynumber == day.getNumber()) return false;
        }
        return true;
    }

    /**
     * This method returns a string that contains every availability of the person
     * @return a string that contains every availability
     */
    public String printAvailability(){
        if (availability.size() == 0){
            return "Non ci sono disponibilità";
        }
        String result = "";
        for(Day day : availability){
            result = result.concat(day.toStringAvailability());
        }
        return result;
    }

    /**
     * This method returns a string that contains every holiday of the person
     * @return a string that contains every holiday
     */
    public String printHolidays(){
        if (holidays.size() == 0){
            return "-";
        }
        String result = "";
        for(Day day : holidays){
            result = result.concat(day.toStringHolidays());
        }
        return result;
    }

    /**
     * This method sort the availability list based on the comparator
     */
    private void sortAvailability(){
        availability.sort(new SortDaysComparator());
    }

    /**
     * This method sort the holidays list based on the comparator
     */
    private void sortHolidays(){
        holidays.sort(new SortDaysComparator());
    }

    @Override
    public boolean equals(Object p) {
        return (p instanceof Person && ((Person) p).getName().equals(this.name) && ((Person) p).getSurname().equals(this.surname));
    }

    @Override
    public String toString() {
        return "\nNome: " + this.name + "\nCognome: " + this.surname + "\nOre settimanali: " + this.weekHours + "\nOre assegnate: " + this.actualHours + "\n";
    }
}