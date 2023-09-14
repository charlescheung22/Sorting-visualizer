package sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

import visualization.*;

public class QuickNaive<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;

    public QuickNaive(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    public void sort() {
        coloredData = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        stack.push(listSize - 1);

        while (!stack.isEmpty()) {
            int high = stack.pop();
            int low = stack.pop();

            int pivot = partition(list, low, high);

            // If there are elements on left side of pivot, then push left side to stack
            if (pivot - 1 > low) {
                stack.push(low);
                stack.push(pivot - 1);
            }

            // If there are elements on right side of pivot, then push right side to stack
            if (pivot + 1 < high) {
                stack.push(pivot + 1);
                stack.push(high);
            }
        }
        coloredData.add(new SortedData());
        return;
    }

    private int partition(List<T> list, int low, int high) {
        T pivot = list.get(high);  // choose pivot
        int i = low - 1;  // index of smaller element

        for (int j = low; j < high; j++) {  // traverse and rearrange
            coloredData.add(new ComparisonData(j, high));
            if (list.get(j).compareTo(pivot) < 0) {  // Compare
                i++;
                coloredData.add(new SwapData(i, j));  // Swap
//                T temp = list.get(i);
//                list.set(i, list.get(j));
//                list.set(j, temp);

            }
        }
        coloredData.add(new SwapData(i + 1, high));
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));  // Swap pivot with element at i + 1
        list.set(high, temp);

        return i + 1;
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
