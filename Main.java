import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        final int cacheLineCount = 64;
        final int cacheLineSize = 64;
        final int cacheWay = 4;

        final int cacheHitDelay = 4;
        final int cacheMissDelay = 6;

        final int cacheCommandDelay = 1;

        Bus cacheRAM = (bytes -> (bytes + 1) / 2 + 100);
        Bus cacheProcessor = (bytes -> (bytes + 1) / 2);

        Processor lru = new Processor(new LRUCache(cacheLineCount, cacheLineSize, cacheWay, cacheRAM, cacheHitDelay,
                cacheMissDelay), cacheProcessor, cacheCommandDelay);
        Processor plru = new Processor(new BitPLRUCache(cacheLineCount, cacheLineSize, cacheWay, cacheRAM,
                cacheHitDelay, cacheMissDelay), cacheProcessor, cacheCommandDelay);

        solve(lru);
        solve(plru);

        String template = "%s:\thit perc. %3.4f%%\ttime: %d\n";
        System.out.printf(Locale.ENGLISH, template, "LRU", 100 * lru.getHitRate(), lru.getTime());
        System.out.printf(Locale.ENGLISH, template, "pLRU", 100 * plru.getHitRate(), plru.getTime());
    }

    private static void solve(final Processor processor) {
        Program program = new Program(processor);
//      #define M 64
        final int M = 64;
//      #define N 60
        final int N = 60;
//      #define K 32
        final int K = 32;
//      int8 a[M][K];
        int a = program.new2DArray(8 / 8, M * K);
//      int16 b[K][N];
        int b = program.new2DArray(16 / 8, K * N);
//      int32 c[M][N];
        int c = program.new2DArray(32 / 8, M * N);
//
//
//  void mmul(){
//      int8 *pa = a;
        program.newVar();
//      int32 *pc = c;
        program.newVar();
//      for (int y = 0; y < M; y++){
        program.newFor();
        for (int y = 0; y < M; y++) {
//          for (int x = 0; x < N; x++){
            program.newFor();
            for (int x = 0; x < N; x++) {
//              int16 *pb = b;
                program.newVar();
//              int32 s = 0;
                program.newVar();
//              for (int k = 0; k < K; k++){
                program.newFor();
                for (int k = 0; k < K; k++) {
//                  s += pa[k] * pb[x];
                    program.getArrayElement(a, k + K * y);
                    program.getArrayElement(b, x + N * k);
                    program.makeMultiplication();
                    program.makeAddition();
//                  pb += N;
                    program.makeAddition();
//              }
                    program.newForIteration();
                }
//              pc[x] = s;
                program.setArrayElement(c, x + N * y);
//          }
                program.newForIteration();
            }
//          pa += K;
            program.makeAddition();
//          pc += N;
            program.makeAddition();
//      }
            program.newForIteration();
        }
//  }
        program.endFunction();
    }
}

