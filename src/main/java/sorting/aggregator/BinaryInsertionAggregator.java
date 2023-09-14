package sorting.aggregator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import visualization.*;

public class BinaryInsertionAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public BinaryInsertionAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {
        coloredData = new ArrayList<>();

        for (int i = 1; i < listSize; i++) {
            T key = list.get(i);
            int index = binarySearch(list, key, 0, i - 1);

            // If key is not present, derive the insertion point from the returned value
            if (index < 0) {
                index = -(index + 1);
            }

            // Shift all elements rightward by one position
            // Using Collections.rotate() to avoid tedious code. Rotate might be more optimized than a for loop.
            Collections.rotate(list.subList(index, i + 1), 1);

            // Record the shift
            for (int j = i; j > index; j--) {
                coloredData.add(new SwapData(j, j - 1));
            }
        }

        coloredData.add(new TerminatedData());
    }

    private int binarySearch(List<T> list, T key, int left, int right) {
        if (left > right) {  // Base case: returns the index of the next largest element
            return -(left + 1);
        }

        int mid = (left + right) / 2;
        int comparison = list.get(mid).compareTo(key);
        coloredData.add(new ComparisonData(mid, right + 1));

        if (comparison == 0) {
            return mid;
        } else if (comparison > 0) {
            return binarySearch(list, key, left, mid - 1);
        } else {
            return binarySearch(list, key, mid + 1, right);
        }
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
