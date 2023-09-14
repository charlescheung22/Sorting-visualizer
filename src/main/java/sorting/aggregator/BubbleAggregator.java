package sorting.aggregator;

import java.util.ArrayList;
import java.util.List;

import visualization.*;

public class BubbleAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;
    public BubbleAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {
        coloredData = new ArrayList<>();

        for (int i = 0; i < listSize - 1; i++) {
            boolean hasSwapped = false;

            for (int j = 0; j < listSize - i - 1; j++) {
                coloredData.add(new ComparisonData(j, j + 1));

                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    coloredData.add(new SwapData(j, j + 1));
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    hasSwapped = true;
                }
            }

            // No elements swapped in a pass, so the list is sorted
            if (!hasSwapped) {
                break;
            }
        }

        coloredData.add(new TerminatedData());
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
