package sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import visualization.*;

public class MergeNaive<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public MergeNaive(List<T> list) {
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

        while (i < n1 && j < n2) {
            coloredData.add(new ComparisonData(left + i, mid + 1 + j));
            if (list.get(left + i).compareTo(list.get(mid + 1 + j)) <= 0) {

            } else {

            }

            k++;  // since we know
        }
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
