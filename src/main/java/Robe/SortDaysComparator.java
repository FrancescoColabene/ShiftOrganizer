package Robe;

import Giornate.Day;

import java.util.Comparator;

/**
 * This class implements the comparator interface: it's used to order them based on the day number
 */
public class SortDaysComparator implements Comparator<Day> {

    @Override
    public int compare(Day o1, Day o2) {
        return o1.getNumber()-o2.getNumber();
    }
}