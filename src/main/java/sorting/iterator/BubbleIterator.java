package sorting.iterator;

import java.util.List;
import java.util.NoSuchElementException;

import visualization.*;

/**
 * Bubble sort iterator. Technically an interactor class
 * @param <T> element type. Must be comparable
 */
public class BubbleIterator<T extends Comparable<T>> implements ColoredDataIterator {
    private final List<T> list;
    private final int listSize;
    private boolean sorted = false;
    private int comparisonIndex = 0;
    private boolean hasSwapped = false;
    private boolean requiresSwap = false;

    public BubbleIterator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    // Note that getCurrentState() isn't required since the list is passed by reference; we already know the current state

    /**
     * @return false iff the list is sorted
     */
    @Override
    public boolean hasNext() {
        return !sorted;
    }

    /**
     *
     * @return
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public ColoredData next() throws NoSuchElementException {
        if (sorted) {
            throw new NoSuchElementException();
        } else if (comparisonIndex + 1 >= listSize && !hasSwapped) {
            sorted = true;
            return new TerminatedData();
        } else if (comparisonIndex + 1 >= listSize) {  // must have swapped; reset
            comparisonIndex = 0;
            hasSwapped = false;
            // continue to find the next exchange that happened
        }

        if (requiresSwap) {
            T temp = list.get(comparisonIndex);
            list.set(comparisonIndex, list.get(comparisonIndex + 1));
            list.set(comparisonIndex + 1, temp);
            hasSwapped = true;
            comparisonIndex++;
            return new SwapData(comparisonIndex, comparisonIndex + 1);
        } else {
            requiresSwap = list.get(comparisonIndex).compareTo(list.get(comparisonIndex + 1)) > 0;
            comparisonIndex += (requiresSwap) ? 0 : 1;
            return new ComparisonData(comparisonIndex, comparisonIndex + 1);
        }
    }
}
