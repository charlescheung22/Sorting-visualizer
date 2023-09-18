package sorting.aggregator;

import java.util.List;

import visualization.*;

public class RadixLSDIntegerAggregator implements ColoredDataAggregator {
    private List<Integer> list;
    private int listSize;
    private int minValue;
    private int maxValue;
    private int radix;
    private List<ColoredData> coloredData;

    public RadixLSDIntegerAggregator(List<Integer> list, int radix) {
        this.list = list;
        this.radix = radix;
        listSize = list.size();

        int minValue = list.get(0);
        int maxValue = list.get(0);
        for (int i = 1; i < listSize; i++) {
            if (list.get(i) < minValue) {
                minValue = list.get(i);
            } else if (list.get(i) > maxValue) {
                maxValue = list.get(i);
            }
        }

    }

    public RadixLSDIntegerAggregator(List<Integer> list) {
        this(list, 10);
    }

    public void sort() {  // TODO
        int exponent = 1;
        while ((maxValue - minValue) / exponent >= 1) {
            countingSortByDigit(exponent, minValue);
            exponent *= radix;
        }
    };

    private void countingSortByDigit(int exponent, int minValue) {
        int bucketIndex;
        int[] buckets = new int[radix];
        int[] output = new int[listSize];

        // Initialize bucket
        for (int i = 0; i < radix; i++) {
            buckets[i] = 0;
        }

        // Count frequencies
        for (int i = 0; i < listSize; i++) {
            bucketIndex = ((list.get(i) - minValue) / exponent) % radix;
            buckets[bucketIndex]++;
        }

        // Compute cumulates
        for (int i = 1; i < radix; i++) {
            buckets[i] += buckets[i - 1];
        }

        // Move records
        for (int i = listSize - 1; i >= 0; i--) {
            bucketIndex = ((list.get(i) - minValue) / exponent) % radix;
            output[--buckets[bucketIndex]] = list.get(i);
        }

        // Copy back
        for (int i = 0; i < listSize; i++) {
            list.set(i, output[i]);
        }
    }

    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
