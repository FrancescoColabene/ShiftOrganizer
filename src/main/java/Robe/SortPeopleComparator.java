package Robe;

import java.util.Comparator;

/**
 * This class implements the comparator interface and it's used to rule the priority queue in the Calendario class.
 */
public class SortPeopleComparator implements Comparator<Person> {

    @Override
    public int compare(Person o1, Person o2) {
        float x = (o1.getActualHours()/ o1.getTotalHours()) - (o2.getActualHours()/ o2.getTotalHours());
        if(x>0) return 1;
        if(x<0) return -1;
        return 0;
    }
}
