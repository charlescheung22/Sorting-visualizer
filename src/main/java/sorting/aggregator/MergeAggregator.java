package sorting.aggregator;

import java.util.ArrayList;
import java.util.List;

import visualization.*;

public class MergeAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public MergeAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    @Override
    public void sort() {
        coloredData = new ArrayList<>();

        // Implementation with bottom-up mergesort
        int size = 1;
        while (size < listSize) {
            int left_start = 0;

            while (left_start < listSize - 1) {
                // compute for current pair of sub-arrays
                int mid = Math.min(left_start + size - 1, listSize - 1);
                int right_end = Math.min(left_start + 2 * size - 1, listSize - 1);

                merge(list, left_start, mid, right_end);
                left_start = right_end + 1;
            }
            size = 2 * size;
        }
    }

    private void merge(List<T> list, int left, int mid, int right) {
        int n1 = (mid + 1) - left;  // size of left sub-array
        int n2 = right - mid;  // size of right sub-array

        int i = 0, j = 0, k = left;  // initial indexes
        // preserve left, mid and right indexes for colored data

        List<T> leftList = list.subList(left, mid + 1);
        List<T> rightList = list.subList(mid + 1, right + 1);

        while (i < n1 && j < n2) {
            if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                list.set(k, leftList.get(i));
                coloredData.add(new SwapData(k, left + i));  // must compare many then set
                i++;
            } else {
                list.set(k, rightList.get(j));
                coloredData.add(new SwapData(k, mid + 1 + j));
                j++;
            }
            k++;
        }

    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
