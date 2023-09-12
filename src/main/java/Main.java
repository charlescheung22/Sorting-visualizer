import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



class Main {
    public static void main(String[] args) {

        final int LIST_SIZE = 100;
        List<Integer> list = IntStream.range(1, LIST_SIZE + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(list);


    }
}