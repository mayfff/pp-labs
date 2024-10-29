import java.util.concurrent.atomic.AtomicInteger;

public class Resources {
    public final int p; // amount of processors
    public final int n; // size of vectors and matrices
    public final int h;

    public int[] B;
    public int[] C;
    public int[] D;
    public int[] E;
    public int[] Z;
    public int[][] MA;
    public int[][] MT;

    // shared
    public int x;
    public int[][] MB;
    public AtomicInteger a;

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
        Z = new int[n];

        MA = new int[n][n];
        MB = new int[n][n];
        MT = new int[n][n];

        a = new AtomicInteger();
    }
}
