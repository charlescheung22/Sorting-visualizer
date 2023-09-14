package sorting;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import visualization.*;

public class DoubleSelectionAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public DoubleSelectionAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {  // TODO I'm somehow losing packets of information!!!
        coloredData = new ArrayList<>();

        int left = 0;
        int right = listSize - 1;
        int min = 0;
        int max = 0;

        while (left < right) {  // Make left and right passes until they meet
            min = left;
            max = right;

            for (int i = left; i <= right; i++) {
                coloredData.add(new ComparisonData(i, min));
                if (list.get(i).compareTo(list.get(min)) < 0) {
                    min = i;
                }

                coloredData.add(new ComparisonData(i, max));
                if (list.get(i).compareTo(list.get(max)) > 0) {
                    max = i;
                }
            }

            if (min != left) {
                Collections.swap(list, min, left);
                coloredData.add(new SwapData(min, left));
            }

            if (max != right) {
                Collections.swap(list, max, right);
                coloredData.add(new SwapData(max, right));
            }

            left++;
            right--;
        }

        coloredData.add(new TerminatedData());
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
