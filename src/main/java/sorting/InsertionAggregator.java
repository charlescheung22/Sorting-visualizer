package sorting;

import visualization.*;

import java.util.ArrayList;
import java.util.List;

public class InsertionAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public InsertionAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {
        coloredData = new ArrayList<>();
        for (int i = 1; i < listSize; i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).compareTo(key) > 0) {
                coloredData.add(new ComparisonData(j, j + 1));
                list.set(j + 1, list.get(j));
                coloredData.add(new SwapData(j, j + 1));
                j--;
            }
            list.set(j + 1, key);

        }
        coloredData.add(new TerminatedData());
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }

}
