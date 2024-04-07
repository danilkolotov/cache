import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BitPLRUCache extends AbstractCache {
    List<Integer> bits;

    public BitPLRUCache(int cacheLineCount, int cacheLineSize, int cacheWay, Bus cacheRAM, int cacheHitDelay,
                        int cacheMissDelay) {
        super(cacheLineCount, cacheLineSize, cacheWay, cacheRAM, cacheHitDelay, cacheMissDelay);
        bits = new ArrayList<>(Collections.nCopies(cacheLineCount, 0));
    }

    @Override
    protected void markUsed(int i) {
        bits.set(i, 1);
        List<Integer> currentSet = getSet(bits, i);
        if (currentSet.stream().allMatch(e -> e == 1)) {
            Collections.fill(currentSet, 0);
            bits.set(i, 1);
        }
    }

    @Override
    protected int findPlace(int address) {
        int setStart = extractSetIndex(address) * cacheWay;
        return getSet(bits, setStart).indexOf(0) + setStart;
    }
}
