public class Timer {
    int time;

    public Timer() {
        this.time = 0;
    }

    public int getTime() {
        return time;
    }

    public void increase(int delta){
        time += delta;
    }
}
