package visualization;

/**
 *  This class is used to represent a check in the sorting algorithm.
 */
public class CheckData implements ColoredData {  // useful for the shuffle algorithm
    public static String color = "blue";
    private final int index1;

    public CheckData (int index1) {
        this.index1 = index1;
    }

    public int getIndex1() {
        return index1;
    }

    public String getColor() {
        return color;
    }
}
