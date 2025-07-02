package Robe;

public class DoubleString {
    private final String first;
    private final String second;

    public DoubleString(String first, String second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return the first string
     */
    public String getFirst() {
        return first;
    }

    /**
     * @return the second string
     */
    public String getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object ds) {
        return (ds instanceof DoubleString &&
                ((DoubleString) ds).getFirst().equals(this.first) &&
                ((DoubleString) ds).getSecond().equals(this.second));
    }
}
