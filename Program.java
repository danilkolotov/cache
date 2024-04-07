import java.util.ArrayList;
import java.util.List;

class Program {
    private final Processor processor;
    private final List<Array2D> lists;
    private int arrayStart;

    public Program(Processor processor) {
        this.processor = processor;
        this.lists = new ArrayList<>();
        arrayStart = 0x40000;
    }

    public int new2DArray(int elementSize, int length) {
        Array2D array = new Array2D(elementSize, length, arrayStart);
        arrayStart += array.size();
        lists.add(array);
        return lists.size() - 1;
    }

    public void newVar() {
        processor.increaseTimer(1);
    }

    public void newFor() {
        processor.increaseTimer(1);
    }

    public void endFunction() {
        processor.increaseTimer(1);
    }

    public void makeAddition() {
        processor.increaseTimer(1);
    }

    public void makeMultiplication() {
        processor.increaseTimer(5);
    }

    public void newForIteration() {
        makeAddition();
    }

    public void getArrayElement(int array, int index) {
        Array2D current = lists.get(array);
        processor.getData(current.getAddress(index), current.elementSize());
    }

    public void setArrayElement(int array, int index) {
        Array2D current = lists.get(array);
        processor.setData(current.getAddress(index), current.elementSize());
    }

    private record Array2D(int elementSize, int length, int offset) {
        public int size() {
            return elementSize * length;
        }

        public int getAddress(int index) {
            return offset + index * elementSize;
        }
    }
}
