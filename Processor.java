class Processor {
    private final AbstractCache cache;
    private final Timer timer;
    private final Bus cacheBus;
    private final int cacheCommandDelay;

    public Processor(AbstractCache cache, Bus cacheBus, int cacheCommandDelay) {
        this.cache = cache;
        this.timer = new Timer();
        this.cacheBus = cacheBus;
        this.cacheCommandDelay = cacheCommandDelay;
    }

    public void increaseTimer(int i) {
        timer.increase(i);
    }

    public void getData(int address, int length) {
        timer.increase(cacheCommandDelay);
        cache.getElement(address, timer);
        timer.increase(cacheBus.transferData(length));
    }

    public void setData(int address, int length) {
        getData(address, length);
    }

    public int getTime() {
        return timer.getTime();
    }

    public double getHitRate() {
        return cache.getHitRate();
    }
}
