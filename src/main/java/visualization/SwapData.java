package visualization;

public class SwapData implements ColoredData {
    public static String color = "red";
    private final int index1;
    private final int index2;

    public SwapData (int index1, int index2) {
        this.index1 = index1;
        this.index2 = index2;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public String getColor() {
        return color;
    }
}
