package sorting.aggregator;

import java.util.List;

import visualization.*;

public class RadixLSDIntegerAggregator implements ColoredDataAggregator {
    private List<Integer> list;
    private int listSize;
    private int radix;
    private List<ColoredData> coloredData;

    public RadixLSDIntegerAggregator(List<Integer> list, int radix) {
        this.list = list;
        this.radix = radix;
        listSize = list.size();
    }

    public RadixLSDIntegerAggregator(List<Integer> list) {
        this(list, 10);
    }

    public void sort(){}; // TODO

    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
