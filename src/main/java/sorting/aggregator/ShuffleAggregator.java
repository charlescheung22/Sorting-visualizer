package sorting.aggregator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import visualization.*;

public class ShuffleAggregator<T extends Comparable<T>> implements ColoredDataAggregator {
    private final List<T> list;
    private final int listSize;
    private List<ColoredData> coloredData;


    public ShuffleAggregator(List<T> list) {
        this.list = list;
        this.listSize = list.size();
    }

    public void sortWithoutTerminationData() {
        sortHelper();
    }

    private void sortHelper() {
        coloredData = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            coloredData.add(new CheckData(i));
            int randomIndex = (int) (Math.random() * listSize);
            Collections.swap(list, i, randomIndex);  // The Fisher-Yates shuffle
            coloredData.add(new SwapData(i, randomIndex));
        }
    }

    @Override
    public void sort() {  // "sort" lmao
        sortHelper();

        coloredData.add(new TerminatedData());
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public List<ColoredData> getColoredData() {
        return coloredData;
    }
}
