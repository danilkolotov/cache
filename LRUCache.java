import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class LRUCache extends AbstractCache {
    private final List<Integer> age;

    public LRUCache(int cacheLineCount, int cacheLineSize, int cacheWay, Bus cacheRAM, int cacheHitDelay, int cacheMissDelay) {
        super(cacheLineCount, cacheLineSize, cacheWay, cacheRAM, cacheHitDelay, cacheMissDelay);
        age = new ArrayList<>();
        for (int i = 0; i < cacheLineCount / cacheWay; i++){
            age.addAll(IntStream.range(0, cacheWay).boxed().toList());
        }
    }

    @Override
    protected void markUsed(int i) {
        int current = age.get(i);
        getSet(age, i).replaceAll(a -> (a < current ? a + 1 : a));
        age.set(i, 0);
    }

    @Override
    protected int findPlace(int address) {
        int setStart = extractSetIndex(address) * cacheWay;
        List<Integer> currentSet = getSet(age, setStart);
        return currentSet.indexOf(Collections.max(currentSet)) + setStart;
    }
}
