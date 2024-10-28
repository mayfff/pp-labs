import java.util.concurrent.atomic.AtomicInteger;

public class Resources {
    public final int p;
    public final int n;
    public final int h;

    public int[] B;
    public int[] C;
    public int[] D;
    public int[] E;
    public int[][] MA;

    // shared
    public AtomicInteger x;
    public int[][] MB;

    public Resources(int p, int n) {
        if (n <= 0) throw new IllegalArgumentException("Invalid size of vectors and matrices");
        if (n % p != 0) throw new IllegalArgumentException("n must be a multiple of p");

        this.p = p;
        this.n = n;
        this.h = n / p;

        B = new int[n];
        C = new int[n];
        D = new int[n];
        E = new int[n];

        MA = new int[n][n];
        MB = new int[n][n];

        x = new AtomicInteger();
    }
}
