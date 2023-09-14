package visualization;

public class SetData implements ColoredData {
    public static String color = "red";
    private final int index;
    private final int value;

    public SetData (int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}
