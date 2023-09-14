package sorting;

import visualization.*;

import java.util.List;
import java.util.ArrayList;

public class SelectionNaive<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;
    public SelectionNaive(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {
        coloredData = new ArrayList<>();
        for (int i = 0; i < listSize - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < listSize; j++) {
                coloredData.add(new ComparisonData(minIndex, j));
                if (list.get(j).compareTo(list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            coloredData.add(new SwapData(i, minIndex));
            T temp = list.get(i);
            list.set(i, list.get(minIndex));
            list.set(minIndex, temp);
        }
        coloredData.add(new TerminatedData());
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
