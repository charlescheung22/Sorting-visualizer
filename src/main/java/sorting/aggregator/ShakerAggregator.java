package sorting.aggregator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import visualization.*;

public class ShakerAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public ShakerAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {

        coloredData = new ArrayList<>();

        int left = 0;
        int right = listSize - 1;
        boolean swapped = true;

        while (left < right && swapped) {
            swapped = false;
            int lastRightSwap = left;  // remember the index of the last swap on the right

            // Forward pass
            for (int index = left; index < right; index++) {
                coloredData.add(new ComparisonData(index, index + 1));

                if (list.get(index).compareTo(list.get(index + 1)) > 0) {
                    Collections.swap(list, index, index + 1);
                    coloredData.add(new SwapData(index, index + 1));
                    lastRightSwap = index;
                    swapped = true;
                }
            }
            right = lastRightSwap;  // We know the boundary for the next pass

            if (!swapped) break; // if no swaps occurred, the list is sorted

            swapped = false; // reset swapped for backward pass
            int lastLeftSwap = right;  // remember the index of the last swap on the left

            // Backward pass
            for (int index = right; index > left; index--) {
                coloredData.add(new ComparisonData(index, index - 1));

                if (list.get(index).compareTo(list.get(index - 1)) < 0) {
                    Collections.swap(list, index, index - 1);
                    coloredData.add(new SwapData(index, index - 1));
                    lastLeftSwap = index;
                    swapped = true;
                }
            }
            left = lastLeftSwap;  // boundary
        }

        coloredData.add(new TerminatedData());
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
