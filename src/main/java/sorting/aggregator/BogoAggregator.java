package sorting.aggregator;

import java.util.ArrayList;
import java.util.List;

import visualization.*;

public class BogoAggregator<T extends Comparable<T>> implements ColoredDataAggregator {  // CAUTION DO NOT USE LOL
    private List<T> list;
    private List<ColoredData> coloredData;
    private static final int TERMINATION_DELAY = 250;  // ms

    public BogoAggregator(List<T> list) {
        this.list = list;
    }

    @Override
    public void sort() { // xd
        coloredData = new ArrayList<>();
        ShuffleAggregator<T> shuffleAggregator = new ShuffleAggregator<>(list);
        long endTime = System.currentTimeMillis() + TERMINATION_DELAY;

        while (System.currentTimeMillis() < endTime && !isSorted(this.list)) {
            shuffleAggregator.sortWithoutTerminationData();
            this.list = shuffleAggregator.getList();
            coloredData.addAll(shuffleAggregator.getColoredData());
        }

        if (System.currentTimeMillis() >= endTime) {
            throw new BogoSortTimeoutException("Bogosort timed out after " + TERMINATION_DELAY + " ms");
        }

        if (isSorted(this.list)) {
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
        return coloredData;
    }
}

class BogoSortTimeoutException extends RuntimeException {
    public BogoSortTimeoutException(String message) {
        super(message);
    }
}
