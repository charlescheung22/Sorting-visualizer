package sorting;

import java.util.ArrayList;
import java.util.List;

import visualization.*;

public class BubbleNaive<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;
    public BubbleNaive(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    public void sort() {
        coloredData = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            for (int j = 0; j < listSize - 1; j++) {
                coloredData.add(new ComparisonData(j, j + 1));
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    coloredData.add(new SwapData(j, j + 1));
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        coloredData.add(new SortedData());
    }

    public void sortNaive() {
        coloredData = new ArrayList<>();
        boolean sorted = false;
        boolean hasSwapped = false;

        while (!sorted) {  // while very bad ! ! !
            for (int i = 0; i < listSize - 1; i++) {
                coloredData.add(new ComparisonData(i, i + 1));
                if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                    coloredData.add(new SwapData(i, i + 1));
                    T temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    hasSwapped = true;
                }
            }
            if (!hasSwapped) {
                sorted = true;
            }
        }
        coloredData.add(new SortedData());
    }

    /**
     * Must be called after using a sort() method
     * @return the colored data
     */
    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
