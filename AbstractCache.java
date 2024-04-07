import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AbstractCache {
    protected final int cacheLineCount, cacheLineSize, cacheWay;
    private final Bus RAMBus;
    private final int cacheHitDelay;
    private final int cacheMissDelay;
    protected List<Integer> lines;
    private int hits, misses;

    public AbstractCache(int cacheLineCount, int cacheLineSize, int cacheWay, Bus RAMBus, int cacheHitDelay, int cacheMissDelay) {
        this.cacheLineCount = cacheLineCount;
        this.cacheLineSize = cacheLineSize;
        this.cacheWay = cacheWay;
        this.RAMBus = RAMBus;
        this.cacheHitDelay = cacheHitDelay;
        this.cacheMissDelay = cacheMissDelay;
        this.lines = new ArrayList<>(Collections.nCopies(cacheLineCount, 0));
    }

    public void getElement(int address, Timer timer) {
        if (hasLine(address)) {
            timer.increase(cacheHitDelay);
            hits++;
        } else {
            timer.increase(cacheMissDelay);
            timer.increase(RAMBus.transferData(cacheLineSize));
            misses++;
            int toReplace = findPlace(address);
            lines.set(toReplace, extractTag(address));
            markUsed(toReplace);
        }
    }

    public void setElement(int address, Timer timer) {
        getElement(address, timer);
    }

    protected boolean hasLine(int address) {
        int start = extractSetIndex(address) * cacheWay;
        int index = getSet(lines, start).indexOf(extractTag(address));
        if (index != -1) {
            markUsed(index + start);
            return true;
        }
        return false;
    }

    protected int extractSetIndex(int address) {
        return (address / (cacheLineSize)) % (cacheLineCount / cacheWay);
    }

    protected int extractTag(int address) {
        return address / cacheLineSize / (cacheLineCount / cacheWay);
    }

    protected abstract void markUsed(int i);

    protected abstract int findPlace(int address);

    public double getHitRate() {
        return (double) hits / (hits + misses);
    }

    public <T> List<T> getSet(List<T> list, int index) {
        int start = (index / cacheWay) * cacheWay;
        return list.subList(start, start + cacheWay);
    }
}
