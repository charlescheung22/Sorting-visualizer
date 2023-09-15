package sorting.aggregator;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import visualization.*;

public class BogoAggregator<T extends Comparable<T>> implements ColoredDataAggregator {  // CAUTION DO NOT USE LOL
    private Deque<T> list;
    private Deque<ColoredData> coloredData;
    private static final int TERMINATION_DELAY = 5000;  // ms
    private static final int MAX_ITERATIONS = 10000000;  // idk having both a time and space limiter? maybe its useful when used on different machines
    private static final int MAX_DATA_SIZE = 10000;  // the sort will throw out  old shuffles to keep the length of the list under this value

    public BogoAggregator(List<T> list) {
        this.list = new ArrayDeque<>(list);
    }

    @Override
    public void sort() { // xd  // TODO find out where tf this bug is
        coloredData = new ArrayDeque<>();
        ShuffleAggregator<T> shuffleAggregator = new ShuffleAggregator<>(new ArrayList<>(this.list));
        long endTime = System.currentTimeMillis() + TERMINATION_DELAY;
        int iterations = 0;

        while (System.currentTimeMillis() < endTime && iterations < MAX_ITERATIONS && !isSorted(new ArrayList<>(this.list))) { // see TODO comment below
            shuffleAggregator.sortWithoutTerminationData();
            this.list = new ArrayDeque<>(shuffleAggregator.getList());
            coloredData.addAll(shuffleAggregator.getColoredData());  // to the end of list
            iterations++;

            while (coloredData.size() > MAX_DATA_SIZE) {
                coloredData.removeFirst();  // remove from the front of the list
            }
        }

        if (System.currentTimeMillis() >= endTime) {
            throw new BogoSortTimeoutException("Bogosort timed out after " + TERMINATION_DELAY + " ms");
        }

        if (iterations >= MAX_ITERATIONS) {
            throw new BogoSortTimeoutException("Bogosort exceeded " + MAX_ITERATIONS + " iterations");
        }

        if (isSorted(new ArrayList<>(this.list))) {  // TODO potentially make a new isSorted method for thijs
            coloredData.add(new TerminatedData());
        }

    }

    private boolean isSorted(List<T> lst) {
        for (int i = 1; i < lst.size(); i++) {
            coloredData.add(new ComparisonData(i - 1, i));
            if (lst.get(i - 1).compareTo(lst.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ColoredData> getColoredData() {
        return new ArrayList<>(coloredData);
    }
}

class BogoSortTimeoutException extends RuntimeException {
    public BogoSortTimeoutException(String message) {
        super(message);
    }
}
