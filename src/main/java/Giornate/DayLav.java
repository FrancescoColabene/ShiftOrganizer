package Giornate;

import java.time.LocalTime;

/**
 * This class extends the Day class, and adds Equipes and supervisions, with their related methods.
 */
public class DayLav extends Day {
    private boolean equipe = false;
    private boolean supervision = false;
    private LocalTime timeStart = null;
    private LocalTime timeEnd = null;

    public DayLav(int number, int month, int year) {
        super(number, month, year);

    }

    /**
     * @return the parameter equipe
     */
    public boolean isEquipe() {
        return equipe;
    }

    /**
     * @return the parameter supervision
     */
    public boolean isSupervision() {
        return supervision;
    }

    /**
     * Invert the logical value of equipe
     * @return the final logical value of equipe
     */
    public boolean setEquipe() {
        this.equipe = !this.equipe;
        return this.equipe;
    }

    /**
     * Invert the logical value of supervisione and sets a starting/ending hour
     * @return the final logical value of supervisione
     */
    public boolean setSupervision(int h1, int m1, int h2, int m2) {
        if(this.supervision) {
            this.supervision = false;
            this.timeStart = null;
            this.timeEnd = null;
        }
        else {
            this.supervision = true;
            this.timeStart = LocalTime.of(h1,m1);
            this.timeEnd = LocalTime.of(h2,m2);
        }
        return this.supervision;
    }

    /**
     * Getter for starting hours
     * @return the hour at which starts the supervision
     */
    public int getSHourSuper(){
        return timeStart.getHour();
    }

    /**
     * Getter for starting minute
     * @return the minute at which starts the supervision
     */
    public int getSMinuteSuper(){
        return timeStart.getMinute();
    }

    /**
     * Getter for ending hour
     * @return the hour at which ends the supervision
     */
    public int getEHourSuper(){
        return timeEnd.getHour();
    }

    /**
     * Getter for ending minute
     * @return the minute at which ends the supervision
     */
    public int getEMinuteSuper(){
        return timeEnd.getMinute();
    }
}