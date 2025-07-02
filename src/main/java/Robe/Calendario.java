package Robe;

import Giornate.Bands;
import Giornate.DayLav;
import Giornate.WeekDays;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class represent the calendar: it's the class that contains and use every other one, and the one that interfaces with the view (ik it's bad)
 */
public class Calendario {
    /**
     * List of workers
     */
    private List<Person> dipendenti;
    /**
     * Array of days - starting from 1 (days[0] is not instantiated)
     */
    private final DayLav[] days;
    /**
     * A String-Person map, that associates Full names to the Persons
     */
    private final Map<String, Person> names = new HashMap<>();
    /**
     * A Integer-Person map, that associates codes to the Persons
     */
    private final Map<Integer, Person> codes = new HashMap<>();
    private final int month;
    private final int year;
    private final int numberDays;
    private final int init;
    private final boolean school;

    /**
     * This is the constructor of the class
     * @param init its a variable used to determine if the program is being executed on the IDE (1) or in its final form (0). It changes the paths to load and save workers.
     * @param year the year used to understand if February has 28 or 29 days
     * @param nMonth the required month: 1 for January, 2 for February, etc etc
     * @param school its a variable that communicates to the calendar when to skip the assignation of the first band during the working days (from Monday to Friday) of the week.
     * @throws IOException if there is any problem with the Jackson's reading from file
     */
    public Calendario(int init, int year, int nMonth, boolean school) throws IOException {
        this.init=init;
        this.school=school;
        this.year=year;

        // numberDays setup
        month = nMonth;
        switch(month){
            case 11,4,6,9:
                numberDays = 30;
                break;
            case 2:
                if(year % 4 == 0 && (year % 100 !=0 || (year % 100 == 0 && year % 400 == 0))){
                    numberDays = 29;
                } else {
                    numberDays = 28;
                }
                break;
            default:
                numberDays = 31;
        }
        // creation of the days
        days = new DayLav[numberDays+1];
        for (int i = 1; i < numberDays+1; i++) {
            days[i] = new DayLav(i,month,year);
        }

        dipendenti = new ArrayList<>();

        // where to get workers
        if(init == 1){
            dipendenti = new ObjectMapper().
                    readValue(getClass().getResourceAsStream("/impiegati.json"), new TypeReference<List<Person>>() {});
        }
        else {
            try {
                FileInputStream file;
                String path = "workers\\impiegati.json";
                file = new FileInputStream(path);

                dipendenti = new ObjectMapper().
                        readValue(file, new TypeReference<List<Person>>() {
                        });

            } catch (Exception e) {
                System.out.println("PORCATROIA");
                //File file = new File("\\workers\\impiegati.json");
                //// File dir = new File();
                //if(file.mkdir()){
                //    System.out.println("ok");
                //    if (file.createNewFile()){
                //        System.out.println("vabene");
                //    }
                //}
            }

        }

        // inserting the "null" person with code 0, used in some method as a return
        codes.put(0,new Person("noname","nosurname",0,0,0,false));

        // setting the totalHours of the workers based on the number of days of the month and their weekly payment
        for(Person p : dipendenti){
            p.reset();
            p.setTotalHours((p.getWeekHours())*(4+ (float) (numberDays%7)/7 ));
            names.put(p.getName().toLowerCase(Locale.ROOT)+p.getSurname().toLowerCase(Locale.ROOT),p);
            codes.put(p.getCode(),p);
        }

    }

    /**
     * @return the list of working people
     */
    public List<Person> getWorkers(){
        return new ArrayList<>(dipendenti);
    }

    /**
     * Getter for school parameter
     * @return the boolean value of school
     */
    public boolean getSchool(){
        return this.school;
    }

    /**
     * This method checks if it is school period, if the given day is within the working days (Monday to Friday) and if the given band is the first.
     * @param i to check
     * @param band to check
     * @return true if the turn should not be assigned, false otherwise
     */
    private boolean isSchool(int i, Bands band){
        return school && band.equals(Bands.F1) && !days[i].getName().equals(WeekDays.SABATO) && !days[i].getName().equals(WeekDays.DOMENICA);
    }

    /**
     * Getter for month variable
     * @return the month
     */
    public int getMonth(){
        return month;
    }

    /**
     * Getter for the array days
     * @param n the requested day
     * @return the requested day: return null if it is not istantiated (n=0 or n>numberDays)
     */
    public DayLav getDay(int n){
        if(!checkDays(n)) return null;
        return days[n];
    }

    /**
     * Getter for numberDays variable
     * @return the number of days of the month
     */
    public int getNumberDays() {
        return numberDays;
    }

    /**
     * Getter for year variable
     * @return the year
     */
    public int getYear() {
        return year;
    }


    /**
     * This method check if a day is instantiated.
     * @return true if the requested day exist, false otherwise
     */
    public boolean checkDays(int n) {
        try{
            // the warning is normal: I just care about the exception
            days[n].getNumber();
            return true;
        } catch (NullPointerException e){
            return false;
        }
    }

    /**
     * Inverts the value of the variable equipe of the given day and setup the hours of the workers.
     * @param number of the chosen day
     * @return the final value of equipe
     */
    public boolean setEquipe(int number){
        boolean result = days[number].setEquipe();
        if(result){
            for (Person person : dipendenti){
                person.addSetHours(2.5F);
            }
        }
        else {
            for (Person person : dipendenti){
                person.addSetHours(-2.5F);
            }
        }
        return result;
    }

    /**
     * Inverts the value of the variable supervision of the given day and setup the hours of the workers.
     * @param number of the chosen day
     * @return the final value of supervision
     */
    public boolean setSupervision(int number, int h1, int m1, int h2, int m2){
        boolean result = days[number].setSupervision(h1,m1,h2,m2);
        if(result){
            for (Person person : dipendenti){
                person.addSetHours(1F);
            }
        }
        else {
            for (Person person : dipendenti){
                person.addSetHours(-1F);
            }
        }
        return result;
    }

    /**
     * Adds a new worker to the list if the name-surname pair is unique
     * @param name of the new worker
     * @param surname of the new worker
     * @param hours working weekly hours of the new worker
     * @param nightHours working night of the new worker (not counting the 6.5 that everyone has by default)
     * @param doesNight this parameter tells if the given person can work during nights
     * @return false if the passed parameter for name and surname already corresponds to another person in the list, otherwise true
     */
    public boolean addWorker(String name, String surname, int hours, float nightHours, boolean doesNight){
        if(dipendenti.contains(new Person(name,surname,hours,0,0,false))){
            return false;
        }
        int code = (int) (Math.random() * 100000);
        while(codes.containsKey(code)){
            code = (int) (Math.random() * 100000);
        }
        Person person = new Person(name, surname, hours, code, nightHours, doesNight);
        //todo sistemare sta cosa delle weekhours
        person.setWeekHours(hours,numberDays);
        codes.put(code,person);
        names.put(person.getName().toLowerCase(Locale.ROOT)+person.getSurname().toLowerCase(Locale.ROOT), person);
        dipendenti.add(person);
        for(int i=1;i<numberDays;i++){
            if(days[i].isEquipe()) person.addEquipeHours(Bands.F2);
            if(days[i].isSupervision()) person.addSupervisionHours(Bands.F1);
        }
        return true;
    }

    /**
     * This method removes a worker given his name and his surname
     * @param name of the person to remove
     * @param surname of the person to remove
     * @return true if deleted, false if the person is not found in the list
     */
    public boolean removeWorker(String name, String surname){
        if(dipendenti.remove(new Person(name, surname,0,0, 0, false))){
            final String fullname = name.toLowerCase(Locale.ROOT) + surname.toLowerCase(Locale.ROOT);
            Person removed = names.get(fullname);
            for (int i = 1; i < numberDays; i++) {
                if (days[i].doesWork(removed.getCode())){
                    for (Bands band : Bands.values()) {
                        if (days[i].getCode(band) == removed.getCode()){
                            setWorker(removed,i,band);
                        }
                    }
                }
            }
            codes.remove(names.get(fullname).getCode());
            names.remove(fullname);
            return true;
        }
        return false;
    }

    /**
     * This method returns a Person given his name and his surname (from the map)
     * @param name of the person to get
     * @param surname of the person to get
     * @return the person with "noname" and "nosurname" if the asked person isn't found on the list, the actual person otherwise
     */
    public Person getWorkerFromName(String name, String surname){
        String key = name.toLowerCase(Locale.ROOT) + surname.toLowerCase(Locale.ROOT);
        if(names.containsKey(key)){
            return names.get(key);
        }
        return new Person("noname", "nosurname", 0,0,0, false);
    }

    /**
     * This method returns a Person given his code (from the map)
     * @param code of the person to get
     * @return the person with code 0 if the asked person isn't found on the list, the actual person otherwise
     */
    public Person getWorkerFromCode(int code){
        if(codes.containsKey(code)){
            return codes.get(code);
        }
        return new Person("noname", "nosurname", 0,0,0, false);
    }

    /**
     * This method adds a worker to a specific band using the modifyBand method. IT can also substitute a worker with another one, or removing it from the turn.
     * It always modify the actualHours of the workers included in the operation accordingly.
     * @param dude to set/remove the turn
     * @param day to set
     * @param band to set
     * @return -1 if the worker has availability/holidays in the given day and turn: it does nothing. 0 if the person passed to the method is removed from the turn.
     * 1 if the turn was empty. 2 if there was a substitution.
     */
    public int setWorker(Person dude, int day, Bands band){
        if(!dude.isFree(day,band)){
            return -1;
        }
        Person temp = codes.get(days[day].getCode(band));
        if(!temp.equals(dude) && temp.getCode() != 0){          // substitution
            temp.removeHours(band);
            dude.addHours(band);
            days[day].modifyBand(band,dude.getCode());
            if(days[day].isEquipe()){
                temp.addEquipeHours(band);
                dude.removeEquipeHours(band);
            }

            if(days[day].isSupervision()){
                temp.addSupervisionHours(band);
                dude.removeSupervisionHours(band);
            }
            return 2;
        }
        else if(days[day].modifyBand(band,dude.getCode())){     // empty turn
            dude.addHours(band);
            if(days[day].isEquipe()){
                dude.removeEquipeHours(band);
            }
            if(days[day].isSupervision()){
                dude.removeSupervisionHours(band);
            }
            return 1;
        }
        else {                                                  // removing a turn
            dude.removeHours(band);
            if(days[day].isEquipe()){
                dude.addEquipeHours(band);
            }
            if(days[day].isSupervision()){
                dude.addSupervisionHours(band);
            }
            return 0;
        }
    }

    /**
     * This method control if a given turn is already assigned to a person
     * @param n day to check
     * @param band band to check
     * @return true if the turn is set, false otherwise
     */
    private boolean alreadySet(int n, Bands band){
        return checkDays(n) && days[n].getCode(band) != 0;
    }

    /**
     * This method checks all the different rules to associate a given person to a given turn. REMEMBER: those rules are not checked when you assign a turn.
     * @param n the day to set
     * @param dude the person to set
     * @param band the band to set
     * @return true if the person can be assigned to the turn, false otherwise
     */
    private boolean workingRules(int n, Person dude, Bands band){
        // check totalHours
        if (!dude.canWork()) return false;
        // check availability/holidays
        if (!dude.isFree(days[n].getNumber(), band)) return false;
        // check night turn and doesNight
        if(band.equals(Bands.F5) && !dude.doesNights()) return false;
        // check not working two turns in a single day
        for(Bands b : Bands.values()){
            if(days[n].getCode(b) == dude.getCode()) return false;
        }
        // check not working for 6 consecutive days
        for(int x=n-6;x<n && x>0;x++){
            if(!doesWork(dude,x)){
                break;
            }
            if(x==n-1) return false;
        }
        // check working one night turn per week
        if(band.equals(Bands.F5)) {
            for (int x = n; x > n - 7 && x>0 ; x--) {
                if (days[x].getCode(Bands.F5) == dude.getCode()) return false;
            }
        }
        // check not working the day after the night turn
        return n <= 1 || days[n - 1].getCode(Bands.F5) != dude.getCode();
    }

    /**
     * This method returns true if the person does work in the requested day - checks every band
     * @param dude person to check
     * @param n day to check
     * @return true if he works in one (or more) of the bands, false otherwise
     */
    private boolean doesWork(Person dude, int n){
        for(Bands band : Bands.values()){
            if(days[n].getCode(band) == dude.getCode()){
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns true if the requested day is a weekend day, false otherwise
     * @param i the requested day
     * @return false if i=0 (not instantiated day), or if the day is not a weekend day. true if the day is a weekend day.
     */
    private boolean isWeekend(int i){
        if ( i == 0 ) return false;
        return days[i].getName().equals(WeekDays.SABATO) || days[i].getName().equals(WeekDays.DOMENICA);
    }

    /**
     * This method returns the number of turns of a person
     * @param name of the person
     * @param surname of the person
     * @return -1 if the person is not found, the numbers of turns otherwise
     */
    public int getNumberOfTurns(String name, String surname){
        String key = name.toLowerCase(Locale.ROOT) + surname.toLowerCase(Locale.ROOT);
        if(names.containsKey(key)){
            int code = names.get(key).getCode();
            int x=0;
            for(int i = 1; i < days.length ;  i++){
                for (Bands band : Bands.values()){
                    if(days[i].getCode(band) == code){
                        x++;
                    }
                }
            }
            return x;
        }
        else {
            return -1;
        }
    }

    /**
     * This method assign the working hours to every worker.
     * For days from Monday to Friday, it takes the first worker from a priority queue and see if it can work calling the workingRules method.
     * For weekend days it's different: a given person can't work 2 weekends in a row, and usually who works on Saturday works on Sunday too.
     * Italian explanation of how it works:
     * estraggo il lavoratore con priorità più alta, se non può lavorare lo aggiungo alla lista dei rimossi e ne estraggo un altro
     * finchè la coda non si svuota. Altrimenti modifico l'orario del calendario, aggiungo le ore alla persona, lo rimetto nella coda di priorità insieme
     * a tutti gli altri lavoratori rimossi; cancello la lista dei rimossi ed esco dal ciclo.
     * affronto il sabato ricordandomi chi non può lavorare (dal fine settimana scorso) e poi recupero la lista di chi ha lavorato
     * quel sabato e li faccio lavorare anche di domenica, cioè li metto in una lista di priorità per domenica. se non ho riempito la giornata non importa.
     */
    public void doThings(){

        PriorityQueue<Person> priority = new PriorityQueue<>(dipendenti.size(), new SortPeopleComparator());
        Collections.shuffle(dipendenti);
        priority.addAll(dipendenti);
        List<Person> removed = new ArrayList<>();
        PriorityQueue<Person> weekend = new PriorityQueue<>(dipendenti.size(), new SortPeopleComparator());

        //System.out.println("classifica iniziale");
        //for (Person p : priority) {
        //    System.out.println(p.getActualHours()/ (float) p.getTotalHours() + " " + p.getFullname());
        //}

        Person temp;
        boolean weekFlag;
        weekFlag = days[1].getName().equals(WeekDays.DOMENICA);
        boolean assigned = false;
        List<Bands> bandsList = new ArrayList<>(List.of(Bands.values()));
        for(int i = 1; i < days.length ; i++) {             // why not cycling onto days instead? days[0] is not instantiated
            Collections.shuffle(bandsList);

            if (isWeekend(i)) {
                // weekend
                //System.out.println("Fine settimana:");
                weekend.clear();
                removed.clear();
                if(i<6){
                    // first weekend aka before 6 (at maximum is Saturday 6)
                    if(!weekFlag){
                        // first Saturday: it uses the normal automatic assignment
                        //System.out.println("Sabato" + i);
                        weekFlag = true;
                    } else {
                        // first Sunday (between 1 and 6)
                        //System.out.println("Domenica" + i);
                        weekFlag = false;
                        assigned = true;
                        if( i>1 ) {
                            // first Sunday (after a Saturday)
                            for(Bands band : bandsList){
                                //System.out.println(band + " " + days[i-1].checkBand(band));
                                if(days[i-1].checkBand(band).equals("si")) {
                                    weekend.add(codes.get(days[i-1].getCode(band)));
                                }
                            }
                            //weekend.forEach( p -> System.out.println(p.getFullname()));
                            for(Bands band : bandsList){
                                if(!alreadySet(i,band)) {
                                    temp = weekend.poll();
                                    while (temp != null) {
                                        if (workingRules(i, temp, band)) {
                                            days[i].modifyBand(band, temp.getCode());
                                            temp.addHours(band);
                                            weekend.add(temp);
                                            weekend.addAll(removed);
                                            removed.clear();
                                            break;
                                        } else {
                                            removed.add(temp);
                                            temp = weekend.poll();
                                        }
                                    }
                                    if (weekend.isEmpty()) {
                                        //assigned = false; // remove the comment if you want to complete the day - if it's not completed yet - without the weekend rules.
                                        weekend.addAll(removed);
                                        removed.clear();
                                    }
                                }
                            }
                        }
                        else {
                            // it's the Sunday 1: it uses the normal automatic assignment
                            //System.out.println("è il primo del mese!" + i);
                            assigned = false;
                        }
                    }
                } else {
                    // at least second weekend or Sunday 7
                    assigned = true;
                    if(!weekFlag){
                        // Saturday, takes the people that worked the last weekend and exclude them
                        //System.out.println("Sabato" + i);
                        weekFlag = true;

                        for(Bands band : bandsList){
                            if(i>7 && days[i-7].checkBand(band).equals("si")) {
                                removed.add(codes.get(days[i - 7].getCode(band)));
                            }
                            if(days[i-6].checkBand(band).equals("si")) {
                                removed.add(codes.get(days[i - 6].getCode(band)));
                            }
                        }
                        removed = removed.stream().distinct().collect(Collectors.toList());
                        weekend.addAll(priority);
                        weekend.removeAll(removed);
                        removed.clear();
                        //System.out.println("weekend:");
                        //weekend.forEach( p -> System.out.println(p.getFullname()));

                        // small problem if there are 10 or less workers (with automatic completion of the unfinished weekends):
                        // se un fine settimana lavorano 6 persone, quello dopo ne dovrebbero lavorare 4 ->
                        // manca un turno che con l'autoassegnamento fa lavorare 2 fini settimana di fila una persona ->
                        // succede sempre perché chi lavora sabato notte non lavora mai domenica!

                        for(Bands band : bandsList){
                            if(!alreadySet(i,band)) {
                                temp = weekend.poll();
                                while (temp != null) {
                                    if (workingRules(i, temp, band)) {
                                        days[i].modifyBand(band, temp.getCode());
                                        temp.addHours(band);
                                        weekend.add(temp);
                                        weekend.addAll(removed);
                                        removed.clear();
                                        break;
                                    } else {
                                        removed.add(temp);
                                        temp = weekend.poll();
                                    }
                                }
                                if (weekend.isEmpty()) {
                                    //assigned = false; // remove the comment if you want to complete the day - if it's not completed yet - without the weekend rules.
                                    weekend.addAll(removed);
                                    removed.clear();
                                }
                            }
                        }
                    } else {
                        // Sunday, takes the people that worked Saturday
                        //System.out.println("Domenica" + i);
                        weekFlag = false;
                        for(Bands band : bandsList){
                            if(days[i-1].checkBand(band).equals("si")) {
                                weekend.add(codes.get(days[i-1].getCode(band)));
                            }
                        }
                        //weekend.forEach( p -> System.out.println(p.getFullname()));
                        for(Bands band : bandsList){
                            if(!alreadySet(i,band)) {
                                temp = weekend.poll();
                                while (temp != null) {
                                    if (workingRules(i, temp, band)) {
                                        days[i].modifyBand(band, temp.getCode());
                                        temp.addHours(band);
                                        weekend.add(temp);
                                        weekend.addAll(removed);
                                        removed.clear();
                                        break;
                                    } else {
                                        removed.add(temp);
                                        temp = weekend.poll();
                                    }
                                }
                                if (weekend.isEmpty()) {
                                    //assigned = false; // remove the comment if you want to complete the day - if it's not completed yet - without the weekend rules.
                                    weekend.addAll(removed);
                                    removed.clear();
                                }
                            }
                        }

                    }
                }
                weekend.clear();
            }
            // not weekend - automatic assignment
            if(!assigned) {
                for(Bands band : bandsList){
                    if(isSchool(i,band)){
                        // school period - we skip the first turn
                    }
                    else if(!alreadySet(i,band)) {
                        temp = priority.poll();
                        while (temp != null) {
                            if (workingRules(i, temp, band)) {
                                days[i].modifyBand(band, temp.getCode());
                                temp.addHours(band);
                                priority.add(temp);
                                priority.addAll(removed);
                                removed.clear();
                                break;
                            } else {
                                removed.add(temp);
                                temp = priority.poll();
                            }
                        }
                        if (priority.isEmpty()) {
                            //System.out.println("turno vuoto");
                            priority.addAll(removed);
                            removed.clear();
                        }
                    }
                }
            }
            assigned=false;

            //System.out.println("\nClassifica al giorno " + i);
            //for (Person p : priority) {
            //    System.out.println(p.getActualHours()/ (float) p.getTotalHours() + " " + p.getFullname());
            //}

        }
    }

    /**
     * Takes every worker and saves his information in a json file in the resources directory
     * @throws IOException if there are any json related problems
     */
    public void saveWorkers() throws IOException {
        ObjectWriter ow = new ObjectMapper().writer();
        ow = ow.with(SerializationFeature.INDENT_OUTPUT);
        if (init == 1)
            ow.writeValue(new File("src/main/resources/impiegati.json"), dipendenti);
        else {
            File dir = new File("workers");
            if(dir.mkdir()) System.out.println( "okkkk ");
            ow.writeValue(new File("workers\\impiegati.json"), dipendenti);
        }
    }
}